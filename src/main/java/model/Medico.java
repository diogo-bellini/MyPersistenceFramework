package model;

import framework.Column;
import framework.Entity;
import framework.Inherited;

@Entity
@Inherited
public class Medico extends Usuario {
    @Column
    private String especialidade;

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
}
