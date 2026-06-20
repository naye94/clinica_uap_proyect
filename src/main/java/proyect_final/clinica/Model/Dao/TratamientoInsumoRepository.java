package proyect_final.clinica.Model.Dao;
import proyect_final.clinica.Model.Entity.TratamientoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TratamientoInsumoRepository extends JpaRepository<TratamientoInsumo, Long> {
    
    @Query("SELECT ti FROM TratamientoInsumo ti " +
           "JOIN FETCH ti.insumo " +
           "WHERE ti.tratamiento.idTratamiento = :idTratamiento")
    List<TratamientoInsumo> findByTratamientoIdWithInsumo(@Param("idTratamiento") Long idTratamiento);


        List<TratamientoInsumo> findByTratamiento_IdTratamiento(Long idTratamiento);

}