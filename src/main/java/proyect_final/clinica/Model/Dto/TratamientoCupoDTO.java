package proyect_final.clinica.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TratamientoCupoDTO {
    private Long idTratamiento;
    private String nombreTratamiento;
    private Double precioTratamiento;
    private Integer cuposDisponibles;
}