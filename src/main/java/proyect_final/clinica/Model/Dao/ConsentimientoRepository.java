package proyect_final.clinica.Model.Dao;

import proyect_final.clinica.Model.Entity.Consentimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ConsentimientoRepository extends JpaRepository<Consentimiento, Long> {

    // ===== NUEVO: Buscar por DiagnosticoTratamiento =====
    Optional<Consentimiento> findByDiagnosticoTratamiento_IdDiagTrat(Long idDiagTrat);
    
    // ===== NUEVO: Verificar si existe =====
    boolean existsByDiagnosticoTratamiento_IdDiagTrat(Long idDiagTrat);
    
    // Buscar consentimientos por docente
    List<Consentimiento> findByDocenteIdDocente(Long idDocente);
    
    // Buscar consentimientos por estado
    List<Consentimiento> findByEstado(String estado);
}