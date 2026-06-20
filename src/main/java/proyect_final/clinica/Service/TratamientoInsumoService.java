package proyect_final.clinica.Service;

import proyect_final.clinica.Model.Entity.TratamientoInsumo;
import java.util.List;

public interface TratamientoInsumoService {
    List<TratamientoInsumo> findByTratamientoId(Long idTratamiento);
    TratamientoInsumo guardar(TratamientoInsumo tratamientoInsumo);
    List<TratamientoInsumo> guardarTodos(List<TratamientoInsumo> tratamientoInsumos);
    void eliminar(Long id);
    List<TratamientoInsumo> obtenerTodos();
}