package model;

import framework.Column;
import framework.Entity;
import framework.FrameworkClass;
import framework.Id;

import java.time.LocalDate;

@Entity(tableName = "prescricao")
public class Prescricao{
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData_fim() {
        return data_fim;
    }

    public void setData_fim(LocalDate data_fim) {
        this.data_fim = data_fim;
    }

    public LocalDate getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(LocalDate data_inicio) {
        this.data_inicio = data_inicio;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public Long getDiagnostico_id() {
        return diagnostico_id;
    }

    public void setDiagnostico_id(Long diagnostico_id) {
        this.diagnostico_id = diagnostico_id;
    }
}
