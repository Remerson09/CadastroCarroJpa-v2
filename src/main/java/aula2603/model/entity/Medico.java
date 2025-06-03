package aula2603.model.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entidade Medico, agora herdando de Pessoa.
 * Mantém atributos específicos como crm e a relação com Consultas.
 * Utiliza a estratégia JOINED, mapeando para a tabela 'medicos'.
 */
@Entity
@Table(name = "medicos") // Nome da tabela específica para Medico
@PrimaryKeyJoinColumn(name = "pessoa_id") // Coluna FK que também é PK, referenciando pessoas.id
public class Medico extends Pessoa {

    // id e nome são herdados de Pessoa

    @Column(name = "crm", nullable = false, unique = true) // Mantém o atributo específico CRM
    private String crm;

    // A relação OneToMany com Consulta permanece
    // O mappedBy deve corresponder ao nome do atributo Medico na classe Consulta
    // CascadeType.ALL pode ser perigoso aqui, talvez REMOVE seja mais apropriado se consultas devem ser removidas com o médico
    // Ou deixar sem cascade e gerenciar o ciclo de vida das consultas separadamente.
    // Mantendo CascadeType.ALL conforme o original, mas com ressalvas.
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Consulta> consultas;

    // Construtor padrão
    public Medico() {
        super();
    }

    // Construtor com parâmetros (incluindo o nome da superclasse)
    public Medico(String nome, String crm) {
        super(nome); // Chama o construtor da superclasse
        this.crm = crm;
    }

    // Getters e Setters para atributos específicos
    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    // Métodos auxiliares podem usar getters herdados
    public String dados() {
        // Usa getNome() herdado
        return "Médico: " + getNome() + " | CRM: " + crm;
    }

    public String consultas() {
        StringBuilder sb = new StringBuilder();
        if (consultas != null) {
            for (Consulta consulta : consultas) {
                // Assumindo que consulta.dados() existe e funciona
                sb.append(consulta.dados()).append("\n");
            }
        }
        return sb.toString();
    }

    // Sobrescrever toString() para incluir atributos herdados e específicos
    @Override
    public String toString() {
        return "Medico{" +
                "id=" + getId() + // Usa getId() herdado
                ", nome=\'" + getNome() + "\\\'" + // Usa getNome() herdado
                ", crm=\'" + crm + "\\\'" +
                '}';
    }
}

