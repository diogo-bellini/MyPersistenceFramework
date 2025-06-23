package model;

import framework.Column;
import framework.Entity;
import framework.FrameworkClass;

@Entity(tableName = "TestName")
public class Teste extends FrameworkClass {
    @Column(name = "name")
    private String name;

    @Column(name = "descriptionnnn")
    private String description;

    @Column
    private Boolean myBool;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMyBool() {
        return myBool;
    }

    public void setMyBool(Boolean myBool) {
        this.myBool = myBool;
    }
}
