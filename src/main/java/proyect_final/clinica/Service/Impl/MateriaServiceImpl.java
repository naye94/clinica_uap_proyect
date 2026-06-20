package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Dao.MateriaRepository;
import proyect_final.clinica.Model.Entity.Clinica;
import proyect_final.clinica.Model.Entity.Materia;
import proyect_final.clinica.Service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MateriaServiceImpl implements MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    @Override
    public List<Materia> listarTodos() {
        return materiaRepository.findAll();
    }

    @Override
    public Optional<Materia> obtenerPorId(Long id) {
        return materiaRepository.findById(id);
    }

    @Override
    public Materia guardar(Materia materia) {
        return materiaRepository.save(materia);
    }

    @Override
    public void eliminar(Long id) {
        materiaRepository.deleteById(id);
    }

    @Override
    public List<Materia> obtenerPorClinica(Clinica clinica) {
        return materiaRepository.findByClinica(clinica);
    }
}