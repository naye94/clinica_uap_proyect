package proyect_final.clinica.Model.Dto;
import lombok.*;

@Data
public class SolDetInsumoDTO {
    private Long idInsumo;
    private String nombreInsumo;
    private Integer cantidadSolicitada;
    private Integer cantidadEntregada;
    private String unidadBase;
    private String concentracion;
}

