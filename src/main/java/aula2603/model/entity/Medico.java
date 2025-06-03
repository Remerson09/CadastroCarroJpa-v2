package aula2603.model.entity;

import aula2603.model.entity.Consulta;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String crm;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Consulta> consultas;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    public List<Consulta> getConsultas() { return consultas; }
    public void setConsultas(List<Consulta> consultas) { this.consultas = consultas; }

    public String dados() {
        return "Médico: " + nome + " | CRM: " + crm;
    }

    public String consultas() {
        StringBuilder sb = new StringBuilder();
        if (consultas != null) {
            for (Consulta consulta : consultas) {
                sb.append(consulta.dados()).append("\n");
            }
        }
        return sb.toString();
    }
}