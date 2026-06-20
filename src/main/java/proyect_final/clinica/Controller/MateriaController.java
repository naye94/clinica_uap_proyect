package proyect_final.clinica.Controller;

import proyect_final.clinica.Model.Entity.Clinica;
import proyect_final.clinica.Model.Entity.Materia;
import proyect_final.clinica.Service.ClinicaService; 
import proyect_final.clinica.Service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private ClinicaService clinicaService; // Ahora se reconoce

    @GetMapping
    public ResponseEntity<List<Materia>> listar() {
        return ResponseEntity.ok(materiaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Materia> obtener(@PathVariable Long id) {
        Optional<Materia> materia = materiaService.obtenerPorId(id);
        return materia.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Materia> crear(@RequestBody Materia materia) {
        return ResponseEntity.ok(materiaService.guardar(materia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Materia> actualizar(@PathVariable Long id, @RequestBody Materia materiaActualizada) {
        // Buscar la materia existente
        Optional<Materia> optionalMateria = materiaService.obtenerPorId(id);
        if (!optionalMateria.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Obtener la entidad actual y actualizar sus campos
        Materia materiaExistente = optionalMateria.get();
        // Actualiza los campos que vienen en la petición (evita asignar ID)
        materiaExistente.setNombreMateria(materiaActualizada.getNombreMateria());
        materiaExistente.setCodigoMateria(materiaActualizada.getCodigoMateria());
        // Si hay más campos, actualízalos también

        // Guardar los cambios
        return ResponseEntity.ok(materiaService.guardar(materiaExistente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!materiaService.obtenerPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        materiaService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Materia eliminada"));
    }

    @GetMapping("/clinica/{clinicaId}")
    public ResponseEntity<List<Materia>> obtenerPorClinica(@PathVariable Long clinicaId) {
        // Obtener la clínica por su ID
        Optional<Clinica> clinicaOptional = clinicaService.obtenerPorId(clinicaId);
        if (!clinicaOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Obtener las materias asociadas a esa clínica
        List<Materia> materias = materiaService.obtenerPorClinica(clinicaOptional.get());
        return ResponseEntity.ok(materias);
    }
}