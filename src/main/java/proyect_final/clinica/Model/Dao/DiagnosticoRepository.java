package proyect_final.clinica.Model.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import proyect_final.clinica.Model.Entity.Diagnostico;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
    Optional<Diagnostico> findByRevision_IdRevision(Long idRevision);
    Optional<Diagnostico> findByConsulta_IdConsulta(Long idConsulta);
    // ===== BÚSQUEDA POR CRITERIO =====
    List<Diagnostico> findByConsulta_Paciente_Ci(Integer ci);
    
    List<Diagnostico> findByConsulta_Paciente_Persona_NombreContainingIgnoreCase(String nombre);
    
    List<Diagnostico> findByConsulta_Paciente_Persona_ApellidoPaternoContainingIgnoreCase(String apellido);
    
    @Query("SELECT d FROM Diagnostico d WHERE d.consulta.paciente.persona.nombre LIKE %:nombre% AND d.consulta.paciente.persona.apellidoPaterno LIKE %:apellido%")
    List<Diagnostico> findByConsulta_Paciente_Persona_NombreAndApellidoPaterno(
        @Param("nombre") String nombre, 
        @Param("apellido") String apellido
    );
    
    // ===== OTRAS BÚSQUEDAS =====
    List<Diagnostico> findByConsulta_Paciente_IdPaciente(Long idPaciente);
    
    List<Diagnostico> findByConsulta_Estudiante_IdEstudiante(Long idEstudiante);
        @Query(value = "SELECT * FROM buscar_diagnostico_por_id(:idDiagnostico)", nativeQuery = true)
    Optional<Diagnostico> buscarPorFuncionDiagnostico(@Param("idDiagnostico") Long idDiagnostico);
}