package proyect_final.clinica.Controller;

import proyect_final.clinica.Model.Dto.DocenteDTO;
import proyect_final.clinica.Model.Dto.RegistroDocenteDTO;
import proyect_final.clinica.Model.Entity.Clinica;
import proyect_final.clinica.Model.Entity.Docente;
import proyect_final.clinica.Model.Entity.Persona;
import proyect_final.clinica.Model.Entity.Usuario;
import proyect_final.clinica.Service.ClinicaService;
import proyect_final.clinica.Service.DocenteService;
import proyect_final.clinica.Service.PersonaService;
import proyect_final.clinica.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registro-docente")
@CrossOrigin(origins = "*")
public class RegistroDocenteController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DocenteService docenteService;
    
    @Autowired
    private ClinicaService clinicaService;

    // ✅ ENDPOINT PARA CLÍNICAS (CORREGIDO)
    @GetMapping("/clinicas")
    public ResponseEntity<List<Clinica>> listarClinicas() {
        return ResponseEntity.ok(clinicaService.obtenerTodas());
    }

    // ✅ NUEVO ENDPOINT PARA VERIFICAR USUARIO
    @GetMapping("/verificar-usuario/{nombreUsuario}")
    public ResponseEntity<Map<String, Object>> verificarUsuario(@PathVariable String nombreUsuario) {
        Map<String, Object> respuesta = new HashMap<>();
        try {
            boolean existe = usuarioService.existePorNombreUsuario(nombreUsuario);
            respuesta.put("nombre_usuario", nombreUsuario);
            respuesta.put("disponible", !existe);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            respuesta.put("error", "Error al verificar usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    // ✅ NUEVO ENDPOINT PARA VERIFICAR CÓDIGO
    @GetMapping("/verificar-codigo/{codigo}")
    public ResponseEntity<Map<String, Object>> verificarCodigoDocente(@PathVariable Integer codigo) {
        Map<String, Object> respuesta = new HashMap<>();
        try {
            boolean existe = docenteService.existePorCodigoDocente(codigo);
            respuesta.put("codigo_docente", codigo);
            respuesta.put("disponible", !existe);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            respuesta.put("error", "Error al verificar código");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarDocente(@Valid @RequestBody RegistroDocenteDTO dto) {
        try {
            // Verificaciones previas
            if (usuarioService.existePorNombreUsuario(dto.getNombreUsuario())) {
                return ResponseEntity.badRequest().body(
                    Map.of("error", "El nombre de usuario ya está registrado")
                );
            }

            if (docenteService.existePorCodigoDocente(dto.getCodigoDocente())) {
                return ResponseEntity.badRequest().body(
                    Map.of("error", "El código de docente ya está registrado")
                );
            }

            // Crear Persona
            Persona persona = new Persona();
            persona.setNombre(dto.getNombre());
            persona.setApellidoPaterno(dto.getApellidoPaterno());
            persona.setApellidoMaterno(dto.getApellidoMaterno());
            persona.setEdad(dto.getEdad());
            if (dto.getSexo() != null && !dto.getSexo().isEmpty()) {
                persona.setSexo(dto.getSexo().charAt(0));
            }
            Persona personaGuardada = personaService.guardar(persona);

            // Crear Usuario
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(dto.getNombreUsuario());
            usuario.setContraseña(dto.getContraseña());
            usuario.setEstado(dto.getEstadoUsuario());
            usuario.setPersona(personaGuardada);
            usuario.setEmail(dto.getEmail());
            Usuario usuarioGuardado = usuarioService.guardar(usuario);

            // Crear Docente
            Docente docente = new Docente();
            docente.setCodigoDocente(dto.getCodigoDocente());
            docente.setEspecialidad(dto.getEspecialidad());
            docente.setContrato(dto.getContrato());
            docente.setEstado(dto.getEstadoDocente());
            docente.setUsuario(usuarioGuardado);
            
            // Asignar clínica si se proporcionó
            if (dto.getIdClinica() != null) {
                clinicaService.obtenerPorId(dto.getIdClinica())
                    .ifPresent(docente::setClinica);
            }
            
            Docente docenteGuardado = docenteService.guardar(docente);

            // Respuesta
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Docente registrado exitosamente");
            respuesta.put("id_docente", docenteGuardado.getIdDocente());
            respuesta.put("codigo_docente", docenteGuardado.getCodigoDocente());
            respuesta.put("nombre_completo", personaGuardada.getNombre() + " " + 
                                           personaGuardada.getApellidoPaterno());
            respuesta.put("email", usuarioGuardado.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                Map.of("error", "Error al registrar docente: " + e.getMessage())
            );
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<DocenteDTO>> listarTodos() {
        try {
            List<DocenteDTO> docentes = docenteService.obtenerTodosDTO();
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}