package proyect_final.clinica.Service.Impl;

import proyect_final.clinica.Model.Entity.Consentimiento;
import proyect_final.clinica.Model.Entity.ConsentimientoFoto;
import proyect_final.clinica.Model.Dao.ConsentimientoFotoRepository;
import proyect_final.clinica.Model.Dao.ConsentimientoRepository;
import proyect_final.clinica.Service.ConsentimientoFotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ConsentimientoFotoServiceImpl implements ConsentimientoFotoService {
    
    @Autowired
    private ConsentimientoFotoRepository consentimientoFotoRepository;
    
    @Autowired
    private ConsentimientoRepository consentimientoRepository;
    
    @Value("${upload.consentimiento.dir:uploads/consentimientos/}")
    private String uploadDir;
    
    @Override
    public List<ConsentimientoFoto> obtenerFotosPorConsentimiento(Long idConsentimiento) {
        return consentimientoFotoRepository.findByConsentimientoIdConsentimiento(idConsentimiento);
    }
     
    @Override
    @Transactional
    public ConsentimientoFoto guardarFoto(Long idConsentimiento, MultipartFile archivo, Integer orden) throws IOException {
        // Validar que el consentimiento existe
        Consentimiento consentimiento = consentimientoRepository.findById(idConsentimiento)
            .orElseThrow(() -> new RuntimeException("Consentimiento no encontrado con ID: " + idConsentimiento));
        
        // Validar tipo de archivo
        String contentType = archivo.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
            throw new IllegalArgumentException("Solo se permiten archivos de imagen (JPEG, PNG) o PDF");
        }
        
        // Crear directorio si no existe
        Path directorio = Paths.get(uploadDir);
        if (!Files.exists(directorio)) {
            Files.createDirectories(directorio);
        }
        
        // Generar nombre único
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }
        String nombreArchivo = UUID.randomUUID().toString() + extension;
        
        // Guardar archivo físicamente
        Path rutaCompleta = directorio.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);
        
        // Crear entidad con tus campos
        ConsentimientoFoto foto = new ConsentimientoFoto();
        foto.setConsentimiento(consentimiento);
        foto.setRutaArchivoConsentimiento("/uploads/consentimientos/" + nombreArchivo);
        foto.setNombreConsentimientoOriginal(nombreOriginal);
        foto.setTipoContenidoConsentimiento(contentType);
        foto.setTamanoConsentimiento(archivo.getSize());
        
        return consentimientoFotoRepository.save(foto);
    }
    
    @Override
    @Transactional
    public List<ConsentimientoFoto> guardarMultiplesFotos(Long idConsentimiento, MultipartFile[] archivos) throws IOException {
        List<ConsentimientoFoto> fotosGuardadas = new ArrayList<>();
        
        if (archivos != null) {
            for (MultipartFile archivo : archivos) {
                if (!archivo.isEmpty()) {
                    try {
                        ConsentimientoFoto foto = guardarFoto(idConsentimiento, archivo, null);
                        fotosGuardadas.add(foto);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error al guardar foto: " + e.getMessage());
                    }
                }
            }
        }
        
        return fotosGuardadas;
    }
    

}