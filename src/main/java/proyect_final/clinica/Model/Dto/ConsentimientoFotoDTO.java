package proyect_final.clinica.Model.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsentimientoFotoDTO {
    private Long id;
    private String rutaArchivo;
    private String nombreOriginal;
    private String tipoContenido;
    private Long tamano;
    
    public ConsentimientoFotoDTO() {}
    
    public ConsentimientoFotoDTO(Long id, String rutaArchivo, String nombreOriginal, String tipoContenido, Long tamano) {
        this.id = id;
        this.rutaArchivo = rutaArchivo;
        this.nombreOriginal = nombreOriginal;
        this.tipoContenido = tipoContenido;
        this.tamano = tamano;
    }
}