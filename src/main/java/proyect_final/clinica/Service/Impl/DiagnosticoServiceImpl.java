package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Entity.Diagnostico;
import proyect_final.clinica.Model.Dao.DiagnosticoRepository;
import proyect_final.clinica.Service.DiagnosticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DiagnosticoServiceImpl implements DiagnosticoService {

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    @Override
    public List<Diagnostico> obtenerTodos() {
        return diagnosticoRepository.findAll();
    }

    @Override
    public Optional<Diagnostico> obtenerPorId(Long id) {
        return diagnosticoRepository.findById(id);
    }

    @Override
    public Diagnostico guardar(Diagnostico diagnostico) {
        return diagnosticoRepository.save(diagnostico);
    }

    @Override
    public void eliminar(Long id) {
        diagnosticoRepository.deleteById(id);
    }



        @Override
    public Optional<Diagnostico> buscarPorIdConFuncionDiagnostico(Long idDiagnostico) {
        return diagnosticoRepository.buscarPorFuncionDiagnostico(idDiagnostico);
    }


}