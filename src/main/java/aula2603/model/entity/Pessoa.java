package aula2603.model.entity;

import jakarta.persistence.*;

/**
 * Superclasse abstrata para representar uma Pessoa no sistema da clínica.
 * Define os atributos comuns (id, nome) e a estratégia de herança JOINED.
 */
@Entity
@Table(name = "pessoas") // Tabela base para a hierarquia
@Inheritance(strategy = InheritanceType.JOINED) // Estratégia de herança
public abstract class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    // Construtor padrão exigido pelo JPA
    public Pessoa() {
    }

    // Construtor com nome
    public Pessoa(String nome) {
        this.nome = nome;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // É uma boa prática sobrescrever equals() e hashCode() em entidades
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Verifica se o objeto é nulo ou se a classe do objeto comparado é diferente
        // Usa getClass() != o.getClass() para garantir que a comparação seja feita apenas com objetos da mesma classe concreta
        // Ou instanceof Pessoa para permitir comparação entre subclasses diferentes (se fizer sentido no domínio)
        // Neste caso, como ID é único na hierarquia, comparar apenas pelo ID pode ser suficiente se não for nulo.
        if (o == null || !(o instanceof Pessoa)) return false;

        Pessoa pessoa = (Pessoa) o;

        // Se ambos os IDs são nulos, considera-se que não são iguais (a menos que sejam a mesma instância, já verificado)
        // Se um ID é nulo e o outro não, não são iguais.
        // Se ambos os IDs não são nulos, compara os IDs.
        return id != null ? id.equals(pessoa.id) : false;
    }

    @Override
    public int hashCode() {
        // Usa o hashCode do ID se não for nulo, caso contrário 0 ou um valor padrão.
        return id != null ? id.hashCode() : 0;
    }

    // Método abstrato opcional para forçar subclasses a implementar
    // public abstract String getIdentificadorPrincipal();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}

