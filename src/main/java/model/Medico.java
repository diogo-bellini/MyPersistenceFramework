package model;

import framework.Column;
import framework.Entity;
import framework.Inherited;

@Entity(tableName = "medico")
@Inherited
public class Medico extends Usuario {
    @Column
    private String especialidade;
}
