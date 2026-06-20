package proyect_final.clinica.Service;

import proyect_final.clinica.Model.Entity.DiagnosticoTratamiento;
import java.util.List;
import java.util.Optional;

public interface DiagnosticoTratamientoService {
    List<DiagnosticoTratamiento> findByDiagnosticoId(Long idDiagnostico);
    Optional<DiagnosticoTratamiento> obtenerPorId(Long id);
    DiagnosticoTratamiento guardar(DiagnosticoTratamiento diagnosticoTratamiento);
}