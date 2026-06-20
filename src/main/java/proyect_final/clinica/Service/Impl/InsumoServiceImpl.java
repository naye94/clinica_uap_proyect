package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Entity.Insumo;
import proyect_final.clinica.Model.Dao.InsumoRepository;
import proyect_final.clinica.Service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InsumoServiceImpl implements InsumoService {
    
    @Autowired
    private InsumoRepository insumoRepository;
    
    @Override
    public List<Insumo> obtenerTodos() {
        return insumoRepository.findAll();
    }
    
    @Override
    public Optional<Insumo> obtenerPorId(Long id) {
        return insumoRepository.findById(id);
    }
    
    @Override
    public Insumo guardar(Insumo insumo) {
        return insumoRepository.save(insumo);
    }
    
    @Override
    public void eliminar(Long id) {
        insumoRepository.deleteById(id);
    }
}