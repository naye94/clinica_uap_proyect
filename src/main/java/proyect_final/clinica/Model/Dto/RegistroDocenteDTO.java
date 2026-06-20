package proyect_final.clinica.Model.Dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistroDocenteDTO {
    // ========== DATOS DE PERSONA ==========
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El apellido paterno es obligatorio")
    private String apellidoPaterno;
    
    private String apellidoMaterno;
    
    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "La edad mínima es 18 años")
    @Max(value = 100, message = "La edad máxima es 100 años")
    private Integer edad;
    
    @NotBlank(message = "El sexo es obligatorio")
    @Pattern(regexp = "^[MF]$", message = "Sexo debe ser M o F")
    private String sexo;
    
    // ========== DATOS DE USUARIO ==========
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres")
    private String nombreUsuario;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contraseña;


    private String email;
    
    private Boolean estadoUsuario = true;
    
    // ========== DATOS ESPECÍFICOS DE DOCENTE ==========
    @NotNull(message = "El código de docente es obligatorio")
    private Integer codigoDocente;
    
    @Size(max = 100, message = "La especialidad no puede exceder 100 caracteres")
    private String especialidad;
    
    @Size(max = 100, message = "El contrato no puede exceder 100 caracteres")
    private String contrato;
    
    @NotNull(message = "El estado del docente es obligatorio")
    private Boolean estadoDocente = true;
    
    // Relación con clínica (opcional)
    private Long idClinica;
}