package proyect_final.clinica.Model.Dto;

import lombok.Data;

@Data
public class InsumoDTO {
    private Long idInsumo;
    private String nombreInsumo;
    private Integer cantidad;
    private String unidadBase;
    private String concentracion;
}
