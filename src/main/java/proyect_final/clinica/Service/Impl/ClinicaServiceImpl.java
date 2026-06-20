package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Entity.Clinica;
import proyect_final.clinica.Model.Dao.ClinicaRepository;
import proyect_final.clinica.Service.ClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClinicaServiceImpl implements ClinicaService {

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Override
    public List<Clinica> obtenerTodas() {
        return clinicaRepository.findAll();
    }

    @Override
    public Optional<Clinica> obtenerPorId(Long id) {
        return clinicaRepository.findById(id);
    }

    @Override
    public Clinica guardar(Clinica clinica) {
        return clinicaRepository.save(clinica);
    }

    @Override
    public void eliminar(Long id) {
        clinicaRepository.deleteById(id);
    }

    @Override
    public List<Clinica> buscarPorNombre(String nombre) {
        return clinicaRepository.findByNombreClinicaContainingIgnoreCase(nombre);
    }

    @Override
    public List<Clinica> buscarPorTurno(String nombreTurno) {
        return clinicaRepository.findByTurno_NombreTurno(nombreTurno);
    }

    @Override
    public List<Clinica> obtenerPorRote(Long roteId) {
        return clinicaRepository.findByRote_IdRote(roteId);
    }
}