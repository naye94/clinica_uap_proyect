package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Entity.Diagnostico;
import proyect_final.clinica.Model.Dao.DiagnosticoRepository;
import proyect_final.clinica.Service.DiagnosticoService;
import proyect_final.clinica.Model.Entity.Paciente;
import proyect_final.clinica.Model.Entity.Persona;
import proyect_final.clinica.Model.Entity.DiagnosticoTratamiento;
import proyect_final.clinica.Service.DiagnosticoTratamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiagnosticoServiceImpl implements DiagnosticoService {

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;
    @Autowired
    private DiagnosticoTratamientoService diagnosticoTratamientoService;
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

    // NUEVO MÉTODO - Toda la lógica de formateo aquí
    @Override
    public Map<String, Object> buscarDiagnosticoFormateado(Long idDiagnostico) {
        // 1. Buscar el diagnóstico
        Diagnostico diagnostico = diagnosticoRepository.buscarPorFuncionDiagnostico(idDiagnostico)
            .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado"));
        
        // 2. Crear el mapa de resultados
        Map<String, Object> resultado = new HashMap<>();
        
        // 3. Datos básicos del diagnóstico
        resultado.put("idDiagnostico", diagnostico.getIdDiagnostico());
        resultado.put("descripcionDiagnostico", diagnostico.getDescripcion());
        
        // 4. Datos del paciente
        if (diagnostico.getConsulta() != null && 
            diagnostico.getConsulta().getPaciente() != null) {
            Paciente paciente = diagnostico.getConsulta().getPaciente();
            resultado.put("idPaciente", paciente.getIdPaciente());
            resultado.put("ci", paciente.getCi());
            
            if (paciente.getPersona() != null) {
                Persona persona = paciente.getPersona();
                resultado.put("nombrePaciente", 
                    persona.getNombre() + " " + 
                    persona.getApellidoPaterno() + " " + 
                    persona.getApellidoMaterno());
            }
        }
        
        // 5. Datos de los tratamientos
        List<DiagnosticoTratamiento> diagTrats = diagnosticoTratamientoService
            .findByDiagnosticoId(diagnostico.getIdDiagnostico());
        
        if (!diagTrats.isEmpty()) {
            List<Map<String, Object>> tratamientos = diagTrats.stream()
                .map(dt -> {
                    Map<String, Object> t = new HashMap<>();
                    t.put("idDiagTrat", dt.getIdDiagTrat());
                    t.put("nombreTratamiento", dt.getTratamiento().getNombreTratamiento());
                    t.put("observaciones", dt.getObservaciones());
                    t.put("dienteAfectado", dt.getDienteAfectado());
                    return t;
                })
                .collect(Collectors.toList());
            resultado.put("diagnosticoTratamientos", tratamientos);
        }
        
        return resultado;
    }
}