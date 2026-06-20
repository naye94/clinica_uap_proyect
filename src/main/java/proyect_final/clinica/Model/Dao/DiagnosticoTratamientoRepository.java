package proyect_final.clinica.Model.Dao;

import proyect_final.clinica.Model.Entity.DiagnosticoTratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DiagnosticoTratamientoRepository extends JpaRepository<DiagnosticoTratamiento, Long> {
    
    List<DiagnosticoTratamiento> findByDiagnostico_IdDiagnostico(Long idDiagnostico);
     
}