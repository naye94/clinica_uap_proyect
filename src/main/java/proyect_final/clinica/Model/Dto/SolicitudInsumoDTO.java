package proyect_final.clinica.Model.Dto;

public class SolicitudInsumoDTO {

    private Long idInsumo;
    private String nombreInsumo;
    private Integer cantidad;

    public SolicitudInsumoDTO() {}

    public SolicitudInsumoDTO(Long idInsumo, String nombreInsumo, Integer cantidad) {
        this.idInsumo = idInsumo;
        this.nombreInsumo = nombreInsumo;
        this.cantidad = cantidad;
    }
    public Long getIdInsumo() {
        return idInsumo;
    }
    public void setIdInsumo(Long idInsumo) {
        this.idInsumo = idInsumo;
    }
    // Getters y Setters
    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}