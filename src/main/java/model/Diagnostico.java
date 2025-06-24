package model;

import framework.Column;
import framework.Entity;
import framework.FrameworkClass;
import framework.Id;

@Entity(tableName = "diagnostico")
public class Diagnostico extends FrameworkClass {
    @Id
    @Column
    private Long id;

    @Column
    private String descricao;

    @Column
    private Long consulta_id;
}
