package proyect_final.clinica.Model.Dao;

import proyect_final.clinica.Model.Entity.Clinica;
import proyect_final.clinica.Model.Entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {
    List<Materia> findByClinica(Clinica clinica);
}