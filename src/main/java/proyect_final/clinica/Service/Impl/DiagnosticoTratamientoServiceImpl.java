package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Entity.DiagnosticoTratamiento;
import proyect_final.clinica.Model.Dao.DiagnosticoTratamientoRepository;
import proyect_final.clinica.Service.DiagnosticoTratamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DiagnosticoTratamientoServiceImpl implements DiagnosticoTratamientoService {

    @Autowired
    private DiagnosticoTratamientoRepository diagnosticoTratamientoRepository;

    @Override
    public List<DiagnosticoTratamiento> findByDiagnosticoId(Long idDiagnostico) {
        return diagnosticoTratamientoRepository.findByDiagnostico_IdDiagnostico(idDiagnostico);
    }

    @Override
    public Optional<DiagnosticoTratamiento> obtenerPorId(Long id) {
        return diagnosticoTratamientoRepository.findById(id);
    }

    @Override
    public DiagnosticoTratamiento guardar(DiagnosticoTratamiento diagnosticoTratamiento) {
        return diagnosticoTratamientoRepository.save(diagnosticoTratamiento);
    }
}