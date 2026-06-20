package proyect_final.clinica.Service;

import proyect_final.clinica.Model.Entity.Consentimiento;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface ConsentimientoService {
    
    List<Consentimiento> obtenerTodos();
    
    Optional<Consentimiento> obtenerPorId(Long id);
    
    // ===== BUSCAR POR DIAGNOSTICO_TRATAMIENTO =====
    Optional<Consentimiento> obtenerPorDiagnosticoTratamiento(Long idDiagTrat);
    
    Consentimiento guardar(Consentimiento consentimiento);
    
    void eliminar(Long id);
    
    // ===== VERIFICAR SI EXISTE =====
    boolean existePorDiagnosticoTratamiento(Long idDiagTrat);

    Consentimiento guardarConFotos(Consentimiento consentimiento, MultipartFile[] fotos) throws IOException;
}