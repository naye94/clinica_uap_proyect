package proyect_final.clinica.Service;

import proyect_final.clinica.Model.Entity.Clinica;
import proyect_final.clinica.Model.Entity.Materia;
import java.util.List;
import java.util.Optional;

public interface MateriaService {
    List<Materia> listarTodos();
    Optional<Materia> obtenerPorId(Long id);
    Materia guardar(Materia materia);
    void eliminar(Long id);
    List<Materia> obtenerPorClinica(Clinica clinica);
}