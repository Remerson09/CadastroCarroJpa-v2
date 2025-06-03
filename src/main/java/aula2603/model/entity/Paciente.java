package aula2603.model.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entidade Paciente, agora herdando de Pessoa.
 * Mantém atributos específicos como telefone e CPF, e a relação com Consultas.
 * Utiliza a estratégia JOINED, mapeando para a tabela 'pacientes'.
 */
@Entity
@Table(name = "pacientes") // Nome da tabela específica para Paciente
@PrimaryKeyJoinColumn(name = "pessoa_id") // Coluna FK que também é PK, referenciando pessoas.id
public class Paciente extends Pessoa {

    // id e nome são herdados de Pessoa

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "cpf", unique = true) // Adicionado CPF, marcado como único (ajuste se necessário)
    private String cpf;

    // A relação OneToMany com Consulta permanece
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.REMOVE)
    private List<Consulta> consultas;

    // Construtor padrão
    public Paciente() {
        super();
    }

    // Construtor com parâmetros (incluindo o nome da superclasse e CPF)
    public Paciente(String nome, String telefone, String cpf) {
        super(nome); // Chama o construtor da superclasse
        this.telefone = telefone;
        this.cpf = cpf;
    }

    // Getters e Setters para atributos específicos
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    // Métodos auxiliares podem usar getters herdados e específicos
    public String dados() {
        return "Paciente: " + getNome() + " | Telefone: " + telefone + " | CPF: " + cpf;
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

    // Sobrescrever toString() para incluir atributos herdados e específicos
    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + getId() +
                ", nome=\'" + getNome() + "\'" +
                ", telefone=\'" + telefone + "\'" +
                ", cpf=\'" + cpf + "\'" +
                '}';
    }
}

