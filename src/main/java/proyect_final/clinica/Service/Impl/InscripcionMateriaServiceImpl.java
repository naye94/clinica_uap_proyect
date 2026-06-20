package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Dao.InscripcionMateriaRepository;
import proyect_final.clinica.Model.Entity.*;
import proyect_final.clinica.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InscripcionMateriaServiceImpl implements InscripcionMateriaService {

    @Autowired
    private InscripcionMateriaRepository inscripcionRepository;

    @Autowired
    private ClinicaService clinicaService;

    @Autowired
    private MateriaService materiaService;

    // @Autowired
    // private CupoService cuposMateriaService;  // Comentar si no se usa

    // @Autowired
    // private TratamientoRealizadoService tratamientoRealizadoService;

    @Override
    public List<InscripcionMateria> listarTodos() {
        return inscripcionRepository.findAll();
    }

    @Override
    public Optional<InscripcionMateria> obtenerPorId(Long id) {
        return inscripcionRepository.findById(id);
    }

    @Override
    public InscripcionMateria guardar(InscripcionMateria inscripcion) {
        return inscripcionRepository.save(inscripcion);
    }

    @Override
    public void eliminar(Long id) {
        inscripcionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void inscribirEstudianteEnPeriodo(Estudiante estudiante, Periodo periodo) {
        // Obtener clínica según el rote actual del estudiante (sin turno)
    List<Clinica> clinicas = clinicaService.obtenerPorRote(estudiante.getRoteActual().getIdRote());
    if (clinicas.isEmpty()) {
        throw new RuntimeException("No se encontró clínica para el rote del estudiante");
    }
    Clinica clinica = clinicas.get(0);
        // Inscribir en materias de la clínica actual
        List<Materia> materiasClinica = materiaService.obtenerPorClinica(clinica);
        for (Materia materia : materiasClinica) {
            if (!existeInscripcion(estudiante, materia, periodo)) {
                crearInscripcion(estudiante, materia, periodo, "cursando");
            }
        }

        // Inscribir en materias aplazadas de períodos anteriores
        List<InscripcionMateria> aplazadas = inscripcionRepository.findByEstudianteAndEstadoInscripcion(estudiante, "aplazada");
        for (InscripcionMateria aplazada : aplazadas) {
            Materia materiaAplazada = aplazada.getMateria();
            if (!existeInscripcion(estudiante, materiaAplazada, periodo)) {
                crearInscripcion(estudiante, materiaAplazada, periodo, "cursando");
            }
        }
    }

    private boolean existeInscripcion(Estudiante estudiante, Materia materia, Periodo periodo) {
        return inscripcionRepository.existsByEstudianteAndMateriaAndPeriodo(estudiante, materia, periodo);
    }

    private void crearInscripcion(Estudiante estudiante, Materia materia, Periodo periodo, String estadoInscripcion) {
        InscripcionMateria inscripcion = new InscripcionMateria();
        inscripcion.setEstudiante(estudiante);
        inscripcion.setMateria(materia);
        inscripcion.setPeriodo(periodo);
        inscripcion.setFechaInscripcion(LocalDate.now());
        inscripcion.setEstadoInscripcion(estadoInscripcion);
        inscripcionRepository.save(inscripcion);
    }

    @Override
    public List<InscripcionMateria> obtenerInscripcionesActivasEstudiante(Long estudianteId) {
        return inscripcionRepository.findInscripcionesActivasByEstudiante(estudianteId);
    }

    @Override
    public List<InscripcionMateria> obtenerPorEstudianteYPeriodo(Estudiante estudiante, Periodo periodo) {
        return inscripcionRepository.findByEstudianteAndPeriodo(estudiante, periodo);
    }

    @Override
    public Optional<InscripcionMateria> obtenerPorEstudianteMateriaPeriodo(Estudiante estudiante, Materia materia, Periodo periodo) {
        return inscripcionRepository.findByEstudianteAndMateriaAndPeriodo(estudiante, materia, periodo);
    }

    // @Override
    // @Transactional
    // public void cerrarPeriodo(Periodo periodo) {
    //     List<InscripcionMateria> inscripciones = inscripcionRepository.findByPeriodoAndEstadoInscripcion(periodo, "cursando");
    //     for (InscripcionMateria inscripcion : inscripciones) {
    //         boolean cumpleCupos = tratamientoRealizadoService.cumpleCuposMateria(inscripcion);
    //         if (cumpleCupos) {
    //             inscripcion.setEstadoInscripcion("aprobada");
    //         } else {
    //             inscripcion.setEstadoInscripcion("aplazada");
    //         }
    //         inscripcionRepository.save(inscripcion);
    //     }
    // }

    @Override
    public Optional<InscripcionMateria> obtenerInscripcionActivaPorEstudianteYMateria(Long estudianteId, Long materiaId) {
        return inscripcionRepository.findByEstudianteAndMateriaAndEstado(estudianteId, materiaId, "cursando");
    }
}