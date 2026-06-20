package proyect_final.clinica.Service;

import proyect_final.clinica.Model.Dto.TratamientoCupoDTO;
import proyect_final.clinica.Model.Entity.Cupos;
import proyect_final.clinica.Model.Entity.Materia;
import proyect_final.clinica.Model.Entity.Tratamiento;
import java.util.List;
import java.util.Optional;

public interface CupoService {
    List<Cupos> listarTodos();
    Optional<Cupos> obtenerPorId(Long id);
    Cupos guardar(Cupos cupo);
    void eliminar(Long id);
    List<Cupos> obtenerPorMateria(Materia materia);
    Optional<Cupos> obtenerPorMateriaYTratamiento(Materia materia, Tratamiento tratamiento);
    List<TratamientoCupoDTO> obtenerTratamientosConCuposPorMateria(Long idMateria);
}