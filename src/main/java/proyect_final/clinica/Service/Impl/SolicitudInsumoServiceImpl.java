package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Entity.*;
import proyect_final.clinica.Model.Dao.*;
import proyect_final.clinica.Service.SolicitudInsumoService;
import proyect_final.clinica.Service.SolDetInsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudInsumoServiceImpl implements SolicitudInsumoService {
    
    @Autowired
    private SolicitudInsumoRepository solicitudInsumoRepository;
    
    @Autowired
    private TratamientoInsumoRepository tratamientoInsumoRepository;
    
    @Autowired
    private SolDetInsumoService solDetInsumoService;
    
    @Autowired
    private DocenteRepository docenteRepository;
    
    @Autowired
    private SolicitudDetInsumoRepository solicitudDetInsumoRepository;
    
    @Override
    public List<SolicitudInsumo> obtenerTodos() {
        return solicitudInsumoRepository.findAll();
    }
    
    @Override
    public Optional<SolicitudInsumo> obtenerPorId(Long id) {
        return solicitudInsumoRepository.findById(id);
    }
    
    @Override
    public SolicitudInsumo guardar(SolicitudInsumo solicitud) {
        return solicitudInsumoRepository.save(solicitud);
    }
    
    @Override
    public void eliminar(Long id) {
        solicitudInsumoRepository.deleteById(id);
    }
    
    // ===== GUARDAR DETALLE =====
    @Override
    public SolicitudDetInsumo guardarDetalle(SolicitudDetInsumo detalle) {
        return solicitudDetInsumoRepository.save(detalle);
    }
    
    // ===== CREAR SOLICITUD DESDE DIAGNOSTICO_TRATAMIENTO =====
    @Override
    @Transactional
    public SolicitudInsumo crearDesdeDiagnosticoTratamiento(DiagnosticoTratamiento diagnosticoTratamiento,
                                                            Usuario usuarioResponsable) {
        
        // 1. Obtener el tratamiento desde DiagnosticoTratamiento
        Tratamiento tratamiento = diagnosticoTratamiento.getTratamiento();
        if (tratamiento == null) {
            throw new RuntimeException("El DiagnosticoTratamiento no tiene un tratamiento asociado");
        }
        
        // 2. Obtener los insumos asociados al tratamiento
        List<TratamientoInsumo> insumosTratamiento = tratamientoInsumoRepository
            .findByTratamiento_IdTratamiento(tratamiento.getIdTratamiento());
        
        // 3. Validar que el tratamiento tenga insumos
        if (insumosTratamiento == null || insumosTratamiento.isEmpty()) {
            throw new RuntimeException("El tratamiento '" + tratamiento.getNombreTratamiento() + 
                                     "' no tiene insumos asociados. No se puede crear la solicitud.");
        }
        
        // 4. Obtener el docente a partir del usuario
        Docente docente = docenteRepository.findByUsuario(usuarioResponsable)
            .orElseThrow(() -> new RuntimeException("No se encontró docente para el usuario: " + 
                                                   usuarioResponsable.getIdUsuario()));
        
        // 5. Crear la solicitud principal con estado PENDIENTE_DOCENTE
        SolicitudInsumo solicitud = new SolicitudInsumo();
        solicitud.setDiagnosticoTratamiento(diagnosticoTratamiento);  // ← CAMBIADO: Usa DiagnosticoTratamiento
        solicitud.setFechaSolicitud(LocalDate.now());
        solicitud.setEstadoSolicitud("PENDIENTE_DOCENTE");
        solicitud = solicitudInsumoRepository.save(solicitud);
        
        // 6. Crear los detalles de la solicitud automáticamente
        for (TratamientoInsumo ti : insumosTratamiento) {
            SolicitudDetInsumo detalle = new SolicitudDetInsumo();
            detalle.setSolicitudInsumo(solicitud);
            detalle.setInsumo(ti.getInsumo());
            detalle.setCantidadSolicitada(ti.getCantidadRequerida());
            detalle.setCantidadEntregada(0);
            detalle.setEstadoSoliDetalle("PENDIENTE_DOCENTE");
            detalle.setDocente(docente);
            detalle.setResponsableInsumo(null);
            
            solDetInsumoService.guardar(detalle);
        }
        
        System.out.println("✅ Solicitud de insumos creada con ID: " + solicitud.getIdSolicitudInsumo());
        System.out.println("📋 Estado inicial: PENDIENTE_DOCENTE");
        System.out.println("👨‍🏫 Docente responsable de aprobación: " + docente.getUsuario().getPersona().getNombre());
        System.out.println("🦷 Tratamiento: " + tratamiento.getNombreTratamiento());
        System.out.println("📦 Insumos registrados: " + insumosTratamiento.size());
        
        return solicitud;
    }

    // ===== MÉTODO DEPRECADO - Mantener por compatibilidad pero usar el nuevo =====
    @Override
    @Transactional
    @Deprecated
    public SolicitudInsumo crearDesdeTratamiento(Tratamiento tratamiento, 
                                                Consentimiento consentimiento,
                                                Usuario usuarioResponsable) {
        throw new UnsupportedOperationException("Este método está obsoleto. Use crearDesdeDiagnosticoTratamiento en su lugar.");
    }

    @Override
    public List<SolicitudInsumo> findByDocenteId(Long idDocente) {
        List<SolicitudDetInsumo> detalles = solicitudDetInsumoRepository.findByDocente_IdDocente(idDocente);
        
        return detalles.stream()
            .map(SolicitudDetInsumo::getSolicitudInsumo)
            .distinct()
            .toList();
    }
    
    @Override
    public List<SolicitudInsumo> findByEstados(List<String> estados) {
        return solicitudInsumoRepository.findByEstadoSolicitudIn(estados);
    }
    
    // ===== NUEVO: Buscar solicitudes por DiagnosticoTratamiento =====
    @Override
    public List<SolicitudInsumo> findByDiagnosticoTratamiento(Long idDiagTrat) {
        return solicitudInsumoRepository.findByDiagnosticoTratamiento_IdDiagTrat(idDiagTrat);
    }
    
   
}