package proyect_final.clinica.Model.Dto;

public class InsumoConsumidoDTO {
    private String clinica;
    private String turno;
    private String insumo;
    private Long totalConsumido;

    public InsumoConsumidoDTO() {}

    public InsumoConsumidoDTO(String clinica, String turno, String insumo, Long totalConsumido) {
        this.clinica = clinica;
        this.turno = turno;
        this.insumo = insumo;
        this.totalConsumido = totalConsumido;
    }

    // Getters y Setters
    public String getClinica() { return clinica; }
    public void setClinica(String clinica) { this.clinica = clinica; }
    
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    
    public String getInsumo() { return insumo; }
    public void setInsumo(String insumo) { this.insumo = insumo; }
    
    public Long getTotalConsumido() { return totalConsumido; }
    public void setTotalConsumido(Long totalConsumido) { this.totalConsumido = totalConsumido; }
}