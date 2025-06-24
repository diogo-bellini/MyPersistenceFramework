package model;

import framework.Column;
import framework.Entity;
import framework.FrameworkClass;
import framework.Id;

@Entity(tableName = "usuario")
public abstract class Usuario extends FrameworkClass {
    @Id
    @Column
    private Long id;

    @Column
    private String email;

    @Column
    private String nome;

    @Column
    private String senha;
}
