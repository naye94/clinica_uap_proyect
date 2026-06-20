package proyect_final.clinica.Model.Dto;

import java.time.LocalDateTime;
import java.util.List;

public class ConsentimientoDTO {

    // ===== CAMBIADO: Usar idDiagnosticoTratamiento =====
    private Long idDiagnosticoTratamiento;  // ← Este es el ID de DiagnosticoTratamiento
    private Long idDocente;
    private Long idEstudiante;
    private Long idMateria;
    
    // Datos copia del tratamiento (para mantener histórico)
    private String nombreTratamiento;
    private String descripcionTratamiento;
    private Double precioTratamiento;

    // Datos del consentimiento
    private String decision;       // "aceptar" o "rechazar"
    private LocalDateTime fecha;

    // Insumos (para la solicitud)
    private List<InsumoDTO> insumos;

    // Constructores
    public ConsentimientoDTO() {}

    // ===== GETTERS Y SETTERS =====
    
    public Long getIdDiagnosticoTratamiento() {
        return idDiagnosticoTratamiento;
    }

    public void setIdDiagnosticoTratamiento(Long idDiagnosticoTratamiento) {
        this.idDiagnosticoTratamiento = idDiagnosticoTratamiento;
    }

    public Long getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Long idDocente) {
        this.idDocente = idDocente;
    }

    public Long getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Long idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Long getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Long idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombreTratamiento() {
        return nombreTratamiento;
    }

    public void setNombreTratamiento(String nombreTratamiento) {
        this.nombreTratamiento = nombreTratamiento;
    }

    public String getDescripcionTratamiento() {
        return descripcionTratamiento;
    }

    public void setDescripcionTratamiento(String descripcionTratamiento) {
        this.descripcionTratamiento = descripcionTratamiento;
    }

    public Double getPrecioTratamiento() {
        return precioTratamiento;
    }

    public void setPrecioTratamiento(Double precioTratamiento) {
        this.precioTratamiento = precioTratamiento;
    }



    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<InsumoDTO> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<InsumoDTO> insumos) {
        this.insumos = insumos;
    }
}