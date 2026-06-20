package proyect_final.clinica.Model.Dto;

public class ClinicaConDocenteDTO {
    private Long idClinica;
    private String nombreClinica;
    private String turno;
    private String nombreDoctor; // Nombre del primer docente

    // Constructor, getters y setters
    public ClinicaConDocenteDTO(Long idClinica, String nombreClinica, String turno, String nombreDoctor) {
        this.idClinica = idClinica;
        this.nombreClinica = nombreClinica;
        this.turno = turno;
        this.nombreDoctor = nombreDoctor;
    }

    // Getters y setters (o usa Lombok)
    public Long getIdClinica() { return idClinica; }
    public String getNombreClinica() { return nombreClinica; }
    public String getTurno() { return turno; }
    public String getNombreDoctor() { return nombreDoctor; }
}