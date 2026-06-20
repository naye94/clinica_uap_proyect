package proyect_final.clinica.Service;

import proyect_final.clinica.Model.Entity.Estudiante;
import proyect_final.clinica.Model.Entity.InscripcionMateria;
import proyect_final.clinica.Model.Entity.Materia;
import proyect_final.clinica.Model.Entity.Periodo;
import java.util.List;
import java.util.Optional;

public interface InscripcionMateriaService {
    List<InscripcionMateria> listarTodos();
    Optional<InscripcionMateria> obtenerPorId(Long id);
    InscripcionMateria guardar(InscripcionMateria inscripcion);
    void eliminar(Long id);
    void inscribirEstudianteEnPeriodo(Estudiante estudiante, Periodo periodo);
    List<InscripcionMateria> obtenerInscripcionesActivasEstudiante(Long estudianteId);
    List<InscripcionMateria> obtenerPorEstudianteYPeriodo(Estudiante estudiante, Periodo periodo);
    Optional<InscripcionMateria> obtenerPorEstudianteMateriaPeriodo(Estudiante estudiante, Materia materia, Periodo periodo);
    // void cerrarPeriodo(Periodo periodo);

    Optional<InscripcionMateria> obtenerInscripcionActivaPorEstudianteYMateria(Long estudianteId, Long materiaId);
    
}