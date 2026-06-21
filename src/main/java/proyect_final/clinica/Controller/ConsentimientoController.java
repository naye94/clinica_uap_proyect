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

    @GetMapping("/buscar-diagnostico")
    @ResponseBody
    public ResponseEntity<?> buscarDiagnostico(@RequestParam String criterio) {
        try {
            Long idDiagnostico = Long.parseLong(criterio.trim());
            
            // El Service hace TODO el trabajo
            Map<String, Object> resultado = diagnosticoService
                .buscarDiagnosticoFormateado(idDiagnostico);
            
            return ResponseEntity.ok(List.of(resultado));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("El ID debe ser un número válido");
        } catch (RuntimeException e) {
            // Diagnóstico no encontrado
            return ResponseEntity.ok(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error al buscar diagnóstico: " + e.getMessage());
        }
    }
    
    @GetMapping("/insumos-por-diag-trat/{idDiagTrat}")
    @ResponseBody
    public ResponseEntity<?> obtenerInsumosPorDiagnosticoTratamiento(@PathVariable Long idDiagTrat) {
        try {
            DiagnosticoTratamiento diagTrat = diagnosticoTratamientoService
                .obtenerPorId(idDiagTrat)
                .orElseThrow(() -> new RuntimeException("No encontrado"));
            
            List<TratamientoInsumo> tratamientoInsumos = tratamientoInsumoService
                .findByTratamientoId(diagTrat.getTratamiento().getIdTratamiento());
            
            if (tratamientoInsumos.isEmpty()) {
                return ResponseEntity.ok(Map.of("mensaje", "Sin insumos asociados", "insumos", new ArrayList<>()));
            }
            
            List<Map<String, Object>> insumos = tratamientoInsumos.stream()
                .map(ti -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idInsumo", ti.getInsumo().getIdInsumo());
                    map.put("nombreInsumo", ti.getInsumo().getNombreInsumo());
                    map.put("cantidadRequerida", ti.getCantidadRequerida());
                    return map;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "insumos", insumos,
                "idDiagTrat", idDiagTrat,
                "nombreTratamiento", diagTrat.getTratamiento().getNombreTratamiento()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/actualizar-diag-trat/{idDiagTrat}")
    @ResponseBody
    public ResponseEntity<?> actualizarDiagnosticoTratamiento(
            @PathVariable Long idDiagTrat,
            @RequestBody Map<String, String> datos) {
        try {
            DiagnosticoTratamiento diagTrat = diagnosticoTratamientoService
                .obtenerPorId(idDiagTrat)
                .orElseThrow(() -> new RuntimeException("No encontrado"));
            
            if (datos.containsKey("observaciones") && datos.get("observaciones") != null) {
                diagTrat.setObservaciones(datos.get("observaciones").trim());
            }
            
            if (datos.containsKey("dienteAfectado") && datos.get("dienteAfectado") != null) {
                diagTrat.setDienteAfectado(datos.get("dienteAfectado").trim());
            }
            
            diagnosticoTratamientoService.guardar(diagTrat);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Actualizado correctamente",
                "idDiagTrat", diagTrat.getIdDiagTrat()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/crear-diagnostico-tratamiento")
    @ResponseBody
    public ResponseEntity<?> crearDiagnosticoTratamiento(@RequestBody Map<String, Object> datos) {
        try {
            Long idDiagnostico = Long.valueOf(datos.get("idDiagnostico").toString());
            Long idTratamiento = Long.valueOf(datos.get("idTratamiento").toString());
            
            Diagnostico diagnostico = diagnosticoService.obtenerPorId(idDiagnostico)
                .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado"));
            
            Tratamiento tratamiento = tratamientoService.obtenerPorId(idTratamiento)
                .orElseThrow(() -> new RuntimeException("Tratamiento no encontrado"));
            
            // Verificar si ya existe
            List<DiagnosticoTratamiento> existentes = diagnosticoTratamientoService
                .findByDiagnosticoId(idDiagnostico);
            
            boolean yaExiste = existentes.stream()
                .anyMatch(dt -> dt.getTratamiento().getIdTratamiento().equals(idTratamiento));
            
            if (yaExiste) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Ya existe esta relación"
                ));
            }
            
            DiagnosticoTratamiento diagTrat = new DiagnosticoTratamiento();
            diagTrat.setDiagnostico(diagnostico);
            diagTrat.setTratamiento(tratamiento);
            diagTrat.setObservaciones(
                datos.get("observaciones") != null ? datos.get("observaciones").toString() : ""
            );
            diagTrat.setDienteAfectado(
                datos.get("dienteAfectado") != null ? datos.get("dienteAfectado").toString() : ""
            );
            
            DiagnosticoTratamiento guardado = diagnosticoTratamientoService.guardar(diagTrat);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Creado correctamente",
                "idDiagTrat", guardado.getIdDiagTrat()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "/crear-desde-diagnostico", consumes = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<?> crearConsentimientoDesdeDiagnostico(
            @RequestParam("datos") String datosJson,
            @RequestParam(value = "fotos", required = false) MultipartFile[] fotos) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ConsentimientoDTO dto = mapper.readValue(datosJson, ConsentimientoDTO.class);
            
            // Validaciones básicas
            if (dto.getIdDiagnosticoTratamiento() == null || 
                dto.getIdDocente() == null || 
                dto.getIdEstudiante() == null || 
                dto.getIdMateria() == null) {
                return ResponseEntity.badRequest().body("Faltan datos requeridos");
            }
            
            if (!dto.getDecision().equals("aceptar") && !dto.getDecision().equals("rechazar")) {
                return ResponseEntity.badRequest().body("Decisión inválida");
            }
            
            DiagnosticoTratamiento diagnosticoTratamiento = diagnosticoTratamientoService
                .obtenerPorId(dto.getIdDiagnosticoTratamiento())
                .orElseThrow(() -> new RuntimeException("DiagnosticoTratamiento no encontrado"));
            
            Docente docente = docenteService.obtenerPorId(dto.getIdDocente())
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
            
            Estudiante estudiante = estudianteService.obtenerPorId(dto.getIdEstudiante())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
            
            // Verificar inscripción
            inscripcionMateriaService
                .obtenerInscripcionActivaPorEstudianteYMateria(dto.getIdEstudiante(), dto.getIdMateria())
                .orElseThrow(() -> new RuntimeException("Estudiante no inscrito en esta materia"));
            
            Consentimiento consentimiento = new Consentimiento();
            consentimiento.setDiagnosticoTratamiento(diagnosticoTratamiento);
            consentimiento.setDocente(docente);
            consentimiento.setEstudiante(estudiante);
            consentimiento.setDecision(dto.getDecision());
            consentimiento.setFecha(LocalDateTime.now());
            consentimiento.setEstado("PENDIENTE");
            
            Consentimiento guardado = consentimientoService.guardarConFotos(consentimiento, fotos);
            
            // Si acepta, crear solicitud de insumos
            if ("aceptar".equals(dto.getDecision())) {
                generarSolicitud(diagnosticoTratamiento, docente);
            }
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Consentimiento guardado",
                "idConsentimiento", guardado.getIdConsentimiento()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/por-diag-trat/{idDiagTrat}")
    @ResponseBody
    public ResponseEntity<?> obtenerPorDiagnosticoTratamiento(@PathVariable Long idDiagTrat) {
        try {
            Optional<Consentimiento> consentimiento = consentimientoService
                .obtenerPorDiagnosticoTratamiento(idDiagTrat);
            
            if (consentimiento.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Consentimiento c = consentimiento.get();
            Map<String, Object> response = new HashMap<>();
            response.put("idConsentimiento", c.getIdConsentimiento());
            response.put("decision", c.getDecision());
            response.put("estado", c.getEstado());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/obtener-docentes")
    @ResponseBody
    public ResponseEntity<?> obtenerDocentes() {
        try {
            List<Docente> docentes = docenteService.obtenerTodosActivos();
            
            List<DocenteDTO> docentesDTO = docentes.stream()
                .map(d -> {
                    String nombre = "Sin nombre";
                    if (d.getUsuario() != null && d.getUsuario().getPersona() != null) {
                        Persona p = d.getUsuario().getPersona();
                        nombre = p.getNombre() + " " + p.getApellidoPaterno();
                    }
                    return new DocenteDTO(d.getIdDocente(), nombre, d.getEspecialidad(), d.getEstado());
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(docentesDTO);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error: " + e.getMessage());
        }
    }

    // Método auxiliar
    private void generarSolicitud(DiagnosticoTratamiento diagnosticoTratamiento, Docente docente) {
        Tratamiento tratamiento = diagnosticoTratamiento.getTratamiento();
        List<TratamientoInsumo> tratamientoInsumos = tratamientoInsumoService
            .findByTratamientoId(tratamiento.getIdTratamiento());
        
        if (tratamientoInsumos.isEmpty()) return;
        
        SolicitudInsumo solicitud = new SolicitudInsumo();
        solicitud.setDiagnosticoTratamiento(diagnosticoTratamiento);
        solicitud.setFechaSolicitud(LocalDate.now());
        solicitud.setEstadoSolicitud("PENDIENTE_DOCENTE");
        
        SolicitudInsumo solicitudGuardada = solicitudInsumoService.guardar(solicitud);
        
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
    }
}