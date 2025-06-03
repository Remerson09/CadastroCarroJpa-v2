package aula2603.model.entity;

import aula2603.model.entity.Consulta;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)  // Adicione unique=true se necessário
    private String nome;

    private String telefone;


    @OneToMany(mappedBy = "paciente")
    private List<Consulta> consultas;
    // Getter e Setter consistentes para version
    // Restante dos getters e setters permanece igual
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public List<Consulta> getConsultas() { return consultas; }
    public void setConsultas(List<Consulta> consultas) { this.consultas = consultas; }

    // Métodos auxiliares permanecem iguais
    public String dados() {
        return "Paciente: " + nome + " | Telefone: " + telefone;
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