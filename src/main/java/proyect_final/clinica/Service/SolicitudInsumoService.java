package proyect_final.clinica.Service;

import proyect_final.clinica.Model.Entity.*;
import java.util.List;
import java.util.Optional;

public interface SolicitudInsumoService {
    
    // CRUD Básico
    List<SolicitudInsumo> obtenerTodos();
    
    Optional<SolicitudInsumo> obtenerPorId(Long id);
    
    SolicitudInsumo guardar(SolicitudInsumo solicitud);
    
    void eliminar(Long id);
    
    // ===== NUEVO: Guardar detalle =====
    SolicitudDetInsumo guardarDetalle(SolicitudDetInsumo detalle);
    
    // ===== NUEVO: Crear desde DiagnosticoTratamiento =====
    SolicitudInsumo crearDesdeDiagnosticoTratamiento(DiagnosticoTratamiento diagnosticoTratamiento,
                                                     Usuario usuarioResponsable);
    
    // ===== DEPRECADO: Mantener por compatibilidad =====
    @Deprecated
    SolicitudInsumo crearDesdeTratamiento(Tratamiento tratamiento, 
                                         Consentimiento consentimiento,
                                         Usuario usuarioResponsable);
    
    // ===== BÚSQUEDAS =====
    List<SolicitudInsumo> findByDocenteId(Long idDocente);
    
    List<SolicitudInsumo> findByEstados(List<String> estados);
    
    List<SolicitudInsumo> findByDiagnosticoTratamiento(Long idDiagTrat);
    
}