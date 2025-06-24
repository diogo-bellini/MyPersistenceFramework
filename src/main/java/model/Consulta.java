package model;

import framework.Column;
import framework.Entity;
import framework.FrameworkClass;
import framework.Id;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(tableName = "consulta")
public class Consulta extends FrameworkClass {
    @Id
    @Column
    private Long id;

    @Column
    private LocalDate data;

    @Column
    private LocalTime hora;

    @Column
    private Long medico_id;

    @Column
    private Long paciente_id;
}
