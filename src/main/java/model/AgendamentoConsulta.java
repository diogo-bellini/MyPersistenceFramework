package model;

import framework.FrameworkClass;
import framework.Column;
import framework.Entity;
import framework.Id;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(tableName = "agendamento_consulta")
public class AgendamentoConsulta extends FrameworkClass {
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
}
