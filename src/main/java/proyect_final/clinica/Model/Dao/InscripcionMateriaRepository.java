package proyect_final.clinica.Model.Dao;

import proyect_final.clinica.Model.Entity.Estudiante;
import proyect_final.clinica.Model.Entity.InscripcionMateria;
import proyect_final.clinica.Model.Entity.Materia;
import proyect_final.clinica.Model.Entity.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface InscripcionMateriaRepository extends JpaRepository<InscripcionMateria, Long> {

    
    List<InscripcionMateria> findByEstudiante(Estudiante estudiante);

    List<InscripcionMateria> findByEstudianteAndEstadoInscripcion(Estudiante estudiante, String estado);

    boolean existsByEstudianteAndMateriaAndPeriodo(Estudiante estudiante, Materia materia, Periodo periodo);

    List<InscripcionMateria> findByEstudianteAndPeriodo(Estudiante estudiante, Periodo periodo);

    Optional<InscripcionMateria> findByEstudianteAndMateriaAndPeriodo(Estudiante estudiante, Materia materia, Periodo periodo);

    List<InscripcionMateria> findByPeriodoAndEstadoInscripcion(Periodo periodo, String estado);

    @Query("SELECT i FROM InscripcionMateria i WHERE i.estudiante.id = :estudianteId AND i.estadoInscripcion = 'cursando'")
    List<InscripcionMateria> findInscripcionesActivasByEstudiante(@Param("estudianteId") Long estudianteId);

    @Query("SELECT i FROM InscripcionMateria i WHERE i.estudiante.id = :estudianteId AND i.materia.id_materia = :materiaId AND i.estadoInscripcion = :estado")
    Optional<InscripcionMateria> findByEstudianteAndMateriaAndEstado(
            @Param("estudianteId") Long estudianteId,
            @Param("materiaId") Long materiaId,
            @Param("estado") String estado);
}


