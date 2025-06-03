package aula2603.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;
    private double valor;
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "paciente_id") // Este nome é importante
    private Paciente paciente;

    @ManyToOne
    private Medico medico;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public String dados() {
        return "Consulta em " + data + " | Valor: R$" + valor +
                " | Paciente: " + paciente.getNome() +
                " | Médico: " + medico.getNome();
    }
}