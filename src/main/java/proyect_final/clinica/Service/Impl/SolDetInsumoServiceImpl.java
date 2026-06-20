package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Entity.SolicitudDetInsumo;
import proyect_final.clinica.Model.Dao.SolicitudDetInsumoRepository;
import proyect_final.clinica.Service.SolDetInsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SolDetInsumoServiceImpl implements SolDetInsumoService {
    
    @Autowired
    private SolicitudDetInsumoRepository solicitudDetInsumoRepository;
    
    @Override
    public List<SolicitudDetInsumo> obtenerTodos() {
        return solicitudDetInsumoRepository.findAll();
    }
    
    @Override
    public Optional<SolicitudDetInsumo> obtenerPorId(Long id) {
        return solicitudDetInsumoRepository.findById(id);
    }
    
    @Override
    public SolicitudDetInsumo guardar(SolicitudDetInsumo detalle) {
        return solicitudDetInsumoRepository.save(detalle);
    }
    
    @Override
    public void eliminar(Long id) {
        solicitudDetInsumoRepository.deleteById(id);
    }
    
    @Override
    public List<SolicitudDetInsumo> obtenerPorSolicitud(Long idSolicitud) {
        // Cambia este método según tu repository
        return solicitudDetInsumoRepository.findBySolicitudInsumo_IdSolicitudInsumo(idSolicitud);
    }
}