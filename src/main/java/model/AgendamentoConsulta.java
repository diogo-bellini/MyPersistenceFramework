package model;

import framework.FrameworkClass;
import framework.Column;
import framework.Entity;
import framework.Id;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(tableName = "agendamento_consulta")
public class AgendamentoConsulta{
    @Id
    @Column
    private Long id;

    @Column
    private LocalDate data;

    @Column
    private LocalTime hora;

    @Column
    private String status;

    @Column
    private Long medico_id;

    @Column
    private Long paciente_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getMedico_id() {
        return medico_id;
    }

    public void setMedico_id(Long medico_id) {
        this.medico_id = medico_id;
    }

    public Long getPaciente_id() {
        return paciente_id;
    }

    public void setPaciente_id(Long paciente_id) {
        this.paciente_id = paciente_id;
    }
}
