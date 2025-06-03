package aula2603.repository;


import aula2603.model.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca pacientes pelo nome usando HQL.
     * A consulta utiliza LIKE para encontrar correspondências parciais,
     * ignorando maiúsculas e minúsculas.
     *
     * @param nome O nome ou parte do nome do paciente a ser buscado
     * @return Lista de pacientes que correspondem ao critério de busca
     */
    @Query("FROM Paciente p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Paciente> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    /**
     * Busca pacientes pelo nome exato usando HQL.
     *
     * @param nome O nome exato do paciente a ser buscado
     * @return Lista de pacientes com o nome especificado
     */
    @Query("FROM Paciente p WHERE p.nome = :nome")
    List<Paciente> findByNomeExato(@Param("nome") String nome);

    /**
     * Busca pacientes pelo nome, ordenados alfabeticamente.
     *
     * @param nome O nome ou parte do nome do paciente a ser buscado
     * @return Lista de pacientes ordenados por nome
     */
    @Query("FROM Paciente p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY p.nome ASC")
    List<Paciente> findByNomeContainingOrderByNomeAsc(@Param("nome") String nome);
}
