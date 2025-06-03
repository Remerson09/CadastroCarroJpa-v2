package aula2603.repository;


import aula2603.model.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    List<Medico> findAllByOrderByNomeAsc();

    /**
     * Busca médicos pelo nome usando HQL.
     * A consulta utiliza LIKE para encontrar correspondências parciais,
     * ignorando maiúsculas e minúsculas.
     *
     * @param nome O nome ou parte do nome do médico a ser buscado
     * @return Lista de médicos que correspondem ao critério de busca
     */
    @Query("FROM Medico m WHERE LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Medico> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    /**
     * Busca médicos pelo nome exato usando HQL.
     *
     * @param nome O nome exato do médico a ser buscado
     * @return Lista de médicos com o nome especificado
     */
    @Query("FROM Medico m WHERE m.nome = :nome")
    List<Medico> findByNomeExato(@Param("nome") String nome);

    /**
     * Busca médicos pelo nome, ordenados alfabeticamente.
     *
     * @param nome O nome ou parte do nome do médico a ser buscado
     * @return Lista de médicos ordenados por nome
     */
    @Query("FROM Medico m WHERE LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY m.nome ASC")
    List<Medico> findByNomeContainingOrderByNomeAsc(@Param("nome") String nome);

    /**
     * Busca médicos pelo CRM usando HQL.
     *
     * @param crm O CRM do médico a ser buscado
     * @return Lista de médicos com o CRM especificado
     */
    @Query("FROM Medico m WHERE m.crm = :crm")
    List<Medico> findByCrm(@Param("crm") String crm);
}
