package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Dao.TratamientoInsumoRepository;
import proyect_final.clinica.Model.Entity.TratamientoInsumo;
import proyect_final.clinica.Service.TratamientoInsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TratamientoInsumoServiceImpl implements TratamientoInsumoService {

    @Autowired
    private TratamientoInsumoRepository tratamientoInsumoRepository;

    @Override
    public List<TratamientoInsumo> findByTratamientoId(Long idTratamiento) {
        return tratamientoInsumoRepository.findByTratamientoIdWithInsumo(idTratamiento);
    }

    @Override
    public TratamientoInsumo guardar(TratamientoInsumo tratamientoInsumo) {
        return tratamientoInsumoRepository.save(tratamientoInsumo);
    }

    @Override
    public List<TratamientoInsumo> guardarTodos(List<TratamientoInsumo> tratamientoInsumos) {
        return tratamientoInsumoRepository.saveAll(tratamientoInsumos);
    }

    @Override
    public void eliminar(Long id) {
        tratamientoInsumoRepository.deleteById(id);
    }

    @Override
    public List<TratamientoInsumo> obtenerTodos() {
        return tratamientoInsumoRepository.findAll();
    }
}