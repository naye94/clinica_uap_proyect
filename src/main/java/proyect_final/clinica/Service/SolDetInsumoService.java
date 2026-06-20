package proyect_final.clinica.Service;

import proyect_final.clinica.Model.Entity.SolicitudDetInsumo;
import java.util.List;
import java.util.Optional;

public interface SolDetInsumoService {
    List<SolicitudDetInsumo> obtenerTodos();
    Optional<SolicitudDetInsumo> obtenerPorId(Long id);
    SolicitudDetInsumo guardar(SolicitudDetInsumo detalle);
    void eliminar(Long id);
    List<SolicitudDetInsumo> obtenerPorSolicitud(Long idSolicitud);
}