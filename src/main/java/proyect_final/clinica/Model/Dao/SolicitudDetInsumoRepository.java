package proyect_final.clinica.Model.Dao;

import proyect_final.clinica.Model.Entity.SolicitudDetInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SolicitudDetInsumoRepository extends JpaRepository<SolicitudDetInsumo, Long> {

    List<SolicitudDetInsumo> findBySolicitudInsumo_IdSolicitudInsumo(Long idSolicitud);
    

    List<SolicitudDetInsumo> findByDocente_IdDocente(Long idDocente);
  
    List<SolicitudDetInsumo> findByEstadoSoliDetalle(String estado);
}
