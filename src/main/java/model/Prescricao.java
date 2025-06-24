package model;

import framework.Column;
import framework.Entity;
import framework.FrameworkClass;
import framework.Id;

import java.time.LocalDate;

@Entity(tableName = "prescricao")
public class Prescricao extends FrameworkClass {
    @Id
    @Column
    private Long id;

    @Column
    private LocalDate data_fim;

    @Column
    private LocalDate data_inicio;

    @Column
    private String dosagem;

    @Column
    private String frequencia;

    @Column
    private String medicamento;

    @Column
    private Long diagnostico_id;

}
