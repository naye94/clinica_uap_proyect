    package proyect_final.clinica.Service;

    import proyect_final.clinica.Model.Entity.Diagnostico;
    import java.util.List;
    import java.util.Optional;

    public interface DiagnosticoService {
        
        List<Diagnostico> obtenerTodos();
        
        Optional<Diagnostico> obtenerPorId(Long id);
        
        Diagnostico guardar(Diagnostico diagnostico);
        
        void eliminar(Long id);
        
        // ===== NUEVO: Buscar por criterio (para el consentimiento) =====
        List<Diagnostico> buscarPorCriterio(String criterio);
        
        List<Diagnostico> obtenerPorPaciente(Long idPaciente);
        
        List<Diagnostico> obtenerPorEstudiante(Long idEstudiante);


            // ===== BUSCAR POR FUNCIÓN SQL =====
        Optional<Diagnostico> buscarPorIdConFuncionDiagnostico(Long idDiagnostico);
        
    }