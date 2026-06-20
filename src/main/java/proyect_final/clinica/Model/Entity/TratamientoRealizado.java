package proyect_final.clinica.Model.Entity;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tratamiento_realizado")
public class TratamientoRealizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trat_realizado")
    private Long idTratRealizado;

    @ManyToOne
    @JoinColumn(name = "id_diag_trat", nullable = false)
    private DiagnosticoTratamiento diagnosticoTratamiento;

    @ManyToOne
    @JoinColumn(name = "id_diente", nullable = false)
    private Diente diente;

    @Column(name = "nro_sesion", nullable = false)
    private Integer nroSesion;

    @Column(name = "fecha_realizacion")
    private LocalDate fechaRealizacion;

    @Column(name = "estado_trata_realizado", length = 30)
    private String estadoTrataRealizado;

    @Column(name = "observaciones")
    private String observaciones;
}