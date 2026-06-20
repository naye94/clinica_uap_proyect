package proyect_final.clinica.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import proyect_final.clinica.Model.Entity.*;
import proyect_final.clinica.Model.Dto.*;
import proyect_final.clinica.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/consentimientos")
public class ConsentimientoController {

    @Autowired
    private ConsentimientoService consentimientoService;

    @Autowired
    private DiagnosticoService diagnosticoService;

    @Autowired
    private DiagnosticoTratamientoService diagnosticoTratamientoService;

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private SolicitudInsumoService solicitudInsumoService;

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private InscripcionMateriaService inscripcionMateriaService;

    @Autowired
    private TratamientoInsumoService tratamientoInsumoService;

    @Autowired
    private TratamientoService tratamientoService;

    // ==================== BUSCAR DIAGNÓSTICO POR ID USANDO FUNCIÓN SQL ====================
    @GetMapping("/buscar-diagnostico")
    @ResponseBody
    public ResponseEntity<?> buscarDiagnostico(@RequestParam String criterio) {
        try {
            if (criterio == null || criterio.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El criterio de búsqueda no puede estar vacío");
            }
            criterio = criterio.trim();
            System.out.println("🔍 Buscar diagnóstico por ID usando función SQL: " + criterio);
            
            // ✅ CONVIERTE EL STRING A LONG
            Long idDiagnostico;
            try {
                idDiagnostico = Long.parseLong(criterio);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("El ID debe ser un número válido");
            }
            
            // ✅ USA LA FUNCIÓN SQL PARA BUSCAR POR ID
            Optional<Diagnostico> diagnosticoOpt = diagnosticoService.buscarPorIdConFuncionDiagnostico(idDiagnostico);
            
            if (diagnosticoOpt.isEmpty()) {
                return ResponseEntity.ok().body(Collections.emptyList());
            }
            
            Diagnostico d = diagnosticoOpt.get();
            System.out.println("✅ Diagnóstico encontrado: " + d.getIdDiagnostico());
            
            List<Map<String, Object>> resultado = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("idDiagnostico", d.getIdDiagnostico());
            map.put("descripcionDiagnostico", d.getDescripcion());
            map.put("fecha", null);
            
            if (d.getConsulta() != null && d.getConsulta().getPaciente() != null) {
                Paciente paciente = d.getConsulta().getPaciente();
                map.put("idPaciente", paciente.getIdPaciente());
                map.put("ci", paciente.getCi());
                
                if (paciente.getPersona() != null) {
                    Persona p = paciente.getPersona();
                    map.put("nombrePaciente", p.getNombre() + " " + p.getApellidoPaterno() + " " + p.getApellidoMaterno());
                    map.put("nombre", p.getNombre());
                    map.put("apellidoPaterno", p.getApellidoPaterno());
                    map.put("apellidoMaterno", p.getApellidoMaterno());
                    map.put("edad", p.getEdad());
                }
            }
            
            // Obtener DiagnosticoTratamiento asociados (USANDO JPA)
            List<DiagnosticoTratamiento> diagTrats = diagnosticoTratamientoService.findByDiagnosticoId(d.getIdDiagnostico());
            if (diagTrats != null && !diagTrats.isEmpty()) {
                List<Map<String, Object>> tratamientos = diagTrats.stream()
                    .map(dt -> {
                        Map<String, Object> tMap = new HashMap<>();
                        tMap.put("idDiagTrat", dt.getIdDiagTrat());
                        tMap.put("idTratamiento", dt.getTratamiento().getIdTratamiento());
                        tMap.put("nombreTratamiento", dt.getTratamiento().getNombreTratamiento());
                        tMap.put("observaciones", dt.getObservaciones());
                        tMap.put("dienteAfectado", dt.getDienteAfectado());
                        return tMap;
                    })
                    .collect(Collectors.toList());
                map.put("diagnosticoTratamientos", tratamientos);
            }
            
            resultado.add(map);
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("Error al buscar diagnóstico: " + e.getMessage());
        }
    }
    
    // ==================== OBTENER INSUMOS POR DIAGNOSTICO_TRATAMIENTO ====================
    @GetMapping("/insumos-por-diag-trat/{idDiagTrat}")
    @ResponseBody
    public ResponseEntity<?> obtenerInsumosPorDiagnosticoTratamiento(@PathVariable Long idDiagTrat) {
        try {
            Optional<DiagnosticoTratamiento> diagTratOpt = diagnosticoTratamientoService.obtenerPorId(idDiagTrat);
            if (diagTratOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "DiagnosticoTratamiento no encontrado"));
            }
            
            DiagnosticoTratamiento diagTrat = diagTratOpt.get();
            Tratamiento tratamiento = diagTrat.getTratamiento();
            
            List<TratamientoInsumo> tratamientoInsumos = tratamientoInsumoService.findByTratamientoId(tratamiento.getIdTratamiento());
            
            if (tratamientoInsumos.isEmpty()) {
                return ResponseEntity.ok().body(Map.of("mensaje", "Este tratamiento no tiene insumos asociados", "insumos", new ArrayList<>()));
            }
            
            List<Map<String, Object>> insumos = tratamientoInsumos.stream()
                .map(ti -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idInsumo", ti.getInsumo().getIdInsumo());
                    map.put("nombreInsumo", ti.getInsumo().getNombreInsumo());
                    map.put("cantidadRequerida", ti.getCantidadRequerida());
                    map.put("unidadBase", ti.getInsumo().getUnidadBase());
                    map.put("concentracion", ti.getInsumo().getConcentracion());
                    return map;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "insumos", insumos,
                "idDiagTrat", idDiagTrat,
                "nombreTratamiento", tratamiento.getNombreTratamiento(),
                "observaciones", diagTrat.getObservaciones(),
                "dienteAfectado", diagTrat.getDienteAfectado()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error al obtener insumos: " + e.getMessage()));
        }
    }

    // ==================== ACTUALIZAR DIAGNOSTICO_TRATAMIENTO ====================
    @PutMapping("/actualizar-diag-trat/{idDiagTrat}")
    @ResponseBody
    public ResponseEntity<?> actualizarDiagnosticoTratamiento(
            @PathVariable Long idDiagTrat,
            @RequestBody Map<String, String> datos) {
        try {
            Optional<DiagnosticoTratamiento> diagTratOpt = diagnosticoTratamientoService.obtenerPorId(idDiagTrat);
            if (diagTratOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "DiagnosticoTratamiento no encontrado"));
            }
            
            DiagnosticoTratamiento diagTrat = diagTratOpt.get();
            
            if (datos.containsKey("observaciones")) {
                String obs = datos.get("observaciones");
                if (obs != null && !obs.trim().isEmpty()) {
                    diagTrat.setObservaciones(obs.trim());
                }
            }
            
            if (datos.containsKey("dienteAfectado")) {
                String diente = datos.get("dienteAfectado");
                if (diente != null && !diente.trim().isEmpty()) {
                    diagTrat.setDienteAfectado(diente.trim());
                }
            }
            
            diagnosticoTratamientoService.guardar(diagTrat);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "DiagnosticoTratamiento actualizado correctamente");
            response.put("idDiagTrat", diagTrat.getIdDiagTrat());
            response.put("observaciones", diagTrat.getObservaciones());
            response.put("dienteAfectado", diagTrat.getDienteAfectado());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error al actualizar: " + e.getMessage()));
        }
    }

    // ==================== CREAR DIAGNOSTICO_TRATAMIENTO ====================
    @PostMapping("/crear-diagnostico-tratamiento")
    @ResponseBody
    public ResponseEntity<?> crearDiagnosticoTratamiento(@RequestBody Map<String, Object> datos) {
        try {
            Long idDiagnostico = Long.valueOf(datos.get("idDiagnostico").toString());
            Long idTratamiento = Long.valueOf(datos.get("idTratamiento").toString());
            String observaciones = datos.get("observaciones") != null ? datos.get("observaciones").toString() : "";
            String dienteAfectado = datos.get("dienteAfectado") != null ? datos.get("dienteAfectado").toString() : "";
            
            Optional<Diagnostico> diagOpt = diagnosticoService.obtenerPorId(idDiagnostico);
            if (diagOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Diagnóstico no encontrado"));
            }
            
            Optional<Tratamiento> tratOpt = tratamientoService.obtenerPorId(idTratamiento);
            if (tratOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Tratamiento no encontrado"));
            }
            
            List<DiagnosticoTratamiento> existentes = diagnosticoTratamientoService.findByDiagnosticoId(idDiagnostico);
            
            boolean yaExiste = existentes.stream()
                .anyMatch(dt -> dt.getTratamiento().getIdTratamiento().equals(idTratamiento));
            
            if (yaExiste) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Ya existe una relación entre este diagnóstico y tratamiento",
                    "idDiagTrat", existentes.stream()
                        .filter(dt -> dt.getTratamiento().getIdTratamiento().equals(idTratamiento))
                        .findFirst()
                        .get()
                        .getIdDiagTrat()
                ));
            }
            
            DiagnosticoTratamiento diagTrat = new DiagnosticoTratamiento();
            diagTrat.setDiagnostico(diagOpt.get());
            diagTrat.setTratamiento(tratOpt.get());
            diagTrat.setObservaciones(observaciones);
            diagTrat.setDienteAfectado(dienteAfectado);
            
            DiagnosticoTratamiento guardado = diagnosticoTratamientoService.guardar(diagTrat);
            
            List<TratamientoInsumo> tratamientoInsumos = tratamientoInsumoService.findByTratamientoId(idTratamiento);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "DiagnosticoTratamiento creado correctamente");
            response.put("idDiagTrat", guardado.getIdDiagTrat());
            response.put("idDiagnostico", guardado.getDiagnostico().getIdDiagnostico());
            response.put("idTratamiento", guardado.getTratamiento().getIdTratamiento());
            response.put("observaciones", guardado.getObservaciones());
            response.put("dienteAfectado", guardado.getDienteAfectado());
            response.put("cantidadInsumos", tratamientoInsumos.size());
            
            return ResponseEntity.ok(response);
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "IDs inválidos: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error al crear DiagnosticoTratamiento: " + e.getMessage()));
        }
    }

    // ==================== CREAR CONSENTIMIENTO DESDE DIAGNOSTICO_TRATAMIENTO ====================
    @PostMapping(value = "/crear-desde-diagnostico", consumes = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<?> crearConsentimientoDesdeDiagnostico(
            @RequestParam("datos") String datosJson,
            @RequestParam(value = "fotos", required = false) MultipartFile[] fotos) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ConsentimientoDTO dto = mapper.readValue(datosJson, ConsentimientoDTO.class);
            
            if (dto.getIdDiagnosticoTratamiento() == null) 
                return badRequest("idDiagnosticoTratamiento requerido");
            if (dto.getIdDocente() == null) 
                return badRequest("idDocente requerido");
            if (dto.getIdEstudiante() == null) 
                return badRequest("idEstudiante requerido");
            if (dto.getIdMateria() == null) 
                return badRequest("idMateria requerido");
            if (dto.getDecision() == null || (!dto.getDecision().equals("aceptar") && !dto.getDecision().equals("rechazar")))
                return badRequest("Decisión inválida");

            Optional<DiagnosticoTratamiento> diagTratOpt = diagnosticoTratamientoService.obtenerPorId(dto.getIdDiagnosticoTratamiento());
            if (diagTratOpt.isEmpty()) {
                return badRequest("DiagnosticoTratamiento no encontrado. Primero debe crearlo con /crear-diagnostico-tratamiento");
            }
            DiagnosticoTratamiento diagnosticoTratamiento = diagTratOpt.get();
            
            Docente docente = docenteService.obtenerPorId(dto.getIdDocente())
                    .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
            Estudiante estudiante = estudianteService.obtenerPorId(dto.getIdEstudiante())
                    .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

            InscripcionMateria inscripcion = inscripcionMateriaService
                    .obtenerInscripcionActivaPorEstudianteYMateria(dto.getIdEstudiante(), dto.getIdMateria())
                    .orElseThrow(() -> new RuntimeException("El estudiante no está inscrito activamente en esta materia"));

            Consentimiento consentimiento = new Consentimiento();
            consentimiento.setDiagnosticoTratamiento(diagnosticoTratamiento);
            consentimiento.setDocente(docente);
            consentimiento.setEstudiante(estudiante);
            consentimiento.setDecision(dto.getDecision());
            consentimiento.setFecha(LocalDateTime.now());
            consentimiento.setEstado("PENDIENTE");

            Consentimiento guardado = consentimientoService.guardarConFotos(consentimiento, fotos);
            System.out.println("✅ Consentimiento creado con ID: " + guardado.getIdConsentimiento());

            if ("aceptar".equals(dto.getDecision())) {
                Tratamiento tratamiento = diagnosticoTratamiento.getTratamiento();
                List<TratamientoInsumo> tratamientoInsumos = tratamientoInsumoService.findByTratamientoId(tratamiento.getIdTratamiento());
                
                if (tratamientoInsumos != null && !tratamientoInsumos.isEmpty()) {
                    SolicitudInsumo solicitud = new SolicitudInsumo();
                    solicitud.setDiagnosticoTratamiento(diagnosticoTratamiento);
                    solicitud.setFechaSolicitud(LocalDate.now());
                    solicitud.setEstadoSolicitud("PENDIENTE_DOCENTE");
                    
                    SolicitudInsumo solicitudGuardada = solicitudInsumoService.guardar(solicitud);
                    System.out.println("✅ SolicitudInsumo creada con ID: " + solicitudGuardada.getIdSolicitudInsumo());
                    
                    for (TratamientoInsumo ti : tratamientoInsumos) {
                        SolicitudDetInsumo detalle = new SolicitudDetInsumo();
                        detalle.setSolicitudInsumo(solicitudGuardada);
                        detalle.setInsumo(ti.getInsumo());
                        detalle.setCantidadSolicitada(ti.getCantidadRequerida());
                        detalle.setCantidadEntregada(0);
                        detalle.setEstadoSoliDetalle("PENDIENTE_DOCENTE");
                        detalle.setDocente(docente);
                        solicitudInsumoService.guardarDetalle(detalle);
                    }
                    System.out.println("✅ Detalles de insumos creados: " + tratamientoInsumos.size());
                }
            }

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Consentimiento guardado correctamente con " + (fotos != null ? fotos.length : 0) + " fotos");
            respuesta.put("idConsentimiento", guardado.getIdConsentimiento());
            respuesta.put("idDiagnosticoTratamiento", dto.getIdDiagnosticoTratamiento());
            respuesta.put("decision", dto.getDecision());
            respuesta.put("fecha", guardado.getFecha().toString());
            respuesta.put("observaciones", diagnosticoTratamiento.getObservaciones());
            respuesta.put("dienteAfectado", diagnosticoTratamiento.getDienteAfectado());
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== OBTENER CONSENTIMIENTO POR DIAGNOSTICO_TRATAMIENTO ====================
    @GetMapping("/por-diag-trat/{idDiagTrat}")
    @ResponseBody
    public ResponseEntity<?> obtenerPorDiagnosticoTratamiento(@PathVariable Long idDiagTrat) {
        try {
            Optional<Consentimiento> consentimiento = consentimientoService.obtenerPorDiagnosticoTratamiento(idDiagTrat);
            if (consentimiento.isPresent()) {
                Consentimiento c = consentimiento.get();
                Map<String, Object> response = new HashMap<>();
                response.put("idConsentimiento", c.getIdConsentimiento());
                response.put("decision", c.getDecision());
                response.put("estado", c.getEstado());
                response.put("fecha", c.getFecha() != null ? c.getFecha().toString() : null);
                
                if (c.getDiagnosticoTratamiento() != null) {
                    DiagnosticoTratamiento dt = c.getDiagnosticoTratamiento();
                    response.put("idDiagTrat", dt.getIdDiagTrat());
                    response.put("nombreTratamiento", dt.getTratamiento() != null ? dt.getTratamiento().getNombreTratamiento() : null);
                    response.put("observaciones", dt.getObservaciones());
                    response.put("dienteAfectado", dt.getDienteAfectado());
                    
                    if (dt.getDiagnostico() != null && dt.getDiagnostico().getConsulta() != null 
                        && dt.getDiagnostico().getConsulta().getPaciente() != null) {
                        Paciente paciente = dt.getDiagnostico().getConsulta().getPaciente();
                        response.put("idPaciente", paciente.getIdPaciente());
                        response.put("nombrePaciente", paciente.getPersona().getNombre() + " " + 
                                                       paciente.getPersona().getApellidoPaterno());
                    }
                }
                
                if (c.getDocente() != null && c.getDocente().getUsuario() != null 
                    && c.getDocente().getUsuario().getPersona() != null) {
                    response.put("nombreDocente", c.getDocente().getUsuario().getPersona().getNombre() + " " +
                                                   c.getDocente().getUsuario().getPersona().getApellidoPaterno());
                }
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ==================== OBTENER DOCENTES ====================
    @GetMapping("/obtener-docentes")
    @ResponseBody
    public ResponseEntity<?> obtenerDocentes() {
        try {
            List<Docente> docentes = docenteService.obtenerTodosActivos();
            
            List<DocenteDTO> docentesDTO = docentes.stream()
                .map(d -> {
                    String nombreCompleto = "Nombre no disponible";
                    if (d.getUsuario() != null && d.getUsuario().getPersona() != null) {
                        Persona persona = d.getUsuario().getPersona();
                        nombreCompleto = (persona.getNombre() + " " + 
                                         persona.getApellidoPaterno() + " " + 
                                         persona.getApellidoMaterno()).trim();
                    }
                    return new DocenteDTO(
                        d.getIdDocente(), 
                        nombreCompleto, 
                        d.getEspecialidad(),
                        d.getEstado()
                    );
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(docentesDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error al obtener docentes: " + e.getMessage());
        }
    }

    private ResponseEntity<?> badRequest(String mensaje) {
        return ResponseEntity.badRequest().body(Map.of("error", mensaje));
    }
}