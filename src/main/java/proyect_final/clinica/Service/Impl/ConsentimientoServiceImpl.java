package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Entity.Consentimiento;
import proyect_final.clinica.Model.Dao.ConsentimientoRepository;
import proyect_final.clinica.Service.ConsentimientoService;
import org.springframework.web.multipart.MultipartFile;
import proyect_final.clinica.Service.ConsentimientoFotoService;
import jakarta.transaction.Transactional;
import java.io.IOException; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConsentimientoServiceImpl implements ConsentimientoService {

    @Autowired
    private ConsentimientoRepository consentimientoRepository;
    
    @Autowired
    private ConsentimientoFotoService consentimientoFotoService;
    
    @Override
    public List<Consentimiento> obtenerTodos() {
        return consentimientoRepository.findAll();
    }

    @Override
    public Optional<Consentimiento> obtenerPorId(Long id) {
        return consentimientoRepository.findById(id);
    }

    // ===== NUEVO: Buscar por DiagnosticoTratamiento =====
    @Override
    public Optional<Consentimiento> obtenerPorDiagnosticoTratamiento(Long idDiagTrat) {
        return consentimientoRepository.findByDiagnosticoTratamiento_IdDiagTrat(idDiagTrat);
    }

    @Override
    public Consentimiento guardar(Consentimiento consentimiento) {
        if (consentimiento.getEstado() == null) {
            consentimiento.setEstado("PENDIENTE");
        }
        return consentimientoRepository.save(consentimiento);
    }

    @Override
    public void eliminar(Long id) {
        Optional<Consentimiento> consentimiento = consentimientoRepository.findById(id);
        if (consentimiento.isPresent()) {
            Consentimiento cons = consentimiento.get();
            cons.setEstado("ANULADO");
            consentimientoRepository.save(cons);
        }
    }

    // ===== NUEVO: Verificar si existe =====
    @Override
    public boolean existePorDiagnosticoTratamiento(Long idDiagTrat) {
        return consentimientoRepository.existsByDiagnosticoTratamiento_IdDiagTrat(idDiagTrat);
    }
    
    @Override
    @Transactional
    public Consentimiento guardarConFotos(Consentimiento consentimiento, MultipartFile[] fotos) throws IOException {
        Consentimiento saved = consentimientoRepository.save(consentimiento);
        
        if (fotos != null && fotos.length > 0) {
            consentimientoFotoService.guardarMultiplesFotos(saved.getIdConsentimiento(), fotos);
        }
        
        return saved;
    }
}