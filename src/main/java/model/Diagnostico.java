package model;

import framework.Column;
import framework.Entity;
import framework.FrameworkClass;
import framework.Id;

@Entity(tableName = "diagnostico")
public class Diagnostico{
    @Id
    @Column
    private Long id;

    @Column
    private String descricao;

    @Column
    private Long consulta_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getConsulta_id() {
        return consulta_id;
    }

    public void setConsulta_id(Long consulta_id) {
        this.consulta_id = consulta_id;
    }
}
