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






    // ===== BUSCAR POR CRITERIO (CI, NOMBRE, ID) =====
    @Override
    public List<Diagnostico> buscarPorCriterio(String criterio) {
        // 1. Intentar buscar por ID (si es número)
        try {
            Long idDiagnostico = Long.parseLong(criterio);
            Optional<Diagnostico> diagnostico = diagnosticoRepository.findById(idDiagnostico);
            if (diagnostico.isPresent()) {
                return List.of(diagnostico.get());
            }
        } catch (NumberFormatException e) {
            // No es un número, continuar con otras búsquedas
        }

        // 2. Intentar buscar por CI (convertir String a Integer)
        try {
            Integer ci = Integer.parseInt(criterio);
            List<Diagnostico> porCi = diagnosticoRepository.findByConsulta_Paciente_Ci(ci);
            if (!porCi.isEmpty()) {
                return porCi;
            }
        } catch (NumberFormatException e) {
            // No es un CI válido, continuar
        }

        // 3. Buscar por nombre del paciente
        String[] partes = criterio.trim().split(" ");
        
        if (partes.length == 1) {
            // Buscar por nombre o apellido individual
            List<Diagnostico> porNombre = diagnosticoRepository
                .findByConsulta_Paciente_Persona_NombreContainingIgnoreCase(criterio);
            if (!porNombre.isEmpty()) {
                return porNombre;
            }
            
            List<Diagnostico> porApellido = diagnosticoRepository
                .findByConsulta_Paciente_Persona_ApellidoPaternoContainingIgnoreCase(criterio);
            if (!porApellido.isEmpty()) {
                return porApellido;
            }
        } else if (partes.length >= 2) {
            // Buscar por nombre y apellido
            String nombre = partes[0];
            String apellido = partes[1];
            return diagnosticoRepository
                .findByConsulta_Paciente_Persona_NombreAndApellidoPaterno(nombre, apellido);
        }

        // 4. Si no se encuentra nada, retornar lista vacía
        return List.of();
    }

    @Override
    public List<Diagnostico> obtenerPorPaciente(Long idPaciente) {
        return diagnosticoRepository.findByConsulta_Paciente_IdPaciente(idPaciente);
    }

    @Override
    public List<Diagnostico> obtenerPorEstudiante(Long idEstudiante) {
        return diagnosticoRepository.findByConsulta_Estudiante_IdEstudiante(idEstudiante);
    }

        @Override
    public Optional<Diagnostico> buscarPorIdConFuncionDiagnostico(Long idDiagnostico) {
        System.out.println("🔍 Buscando diagnóstico por ID usando función SQL: " + idDiagnostico);
        return diagnosticoRepository.buscarPorFuncionDiagnostico(idDiagnostico);
    }


}