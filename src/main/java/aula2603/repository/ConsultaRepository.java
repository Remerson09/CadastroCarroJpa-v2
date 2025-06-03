package aula2603.repository;

import aula2603.model.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    /**
     * Busca todas as consultas de um paciente com os dados do médico e paciente carregados (evita LazyInitializationException).
     */
    @Query("SELECT c FROM Consulta c JOIN FETCH c.paciente JOIN FETCH c.medico WHERE c.paciente.id = :pacienteId")
    List<Consulta> findByPacienteIdWithMedico(@Param("pacienteId") Long pacienteId);

    /**
     * Busca todas as consultas com os dados de paciente e médico já carregados.
     */
    @Query("SELECT DISTINCT c FROM Consulta c LEFT JOIN FETCH c.paciente LEFT JOIN FETCH c.medico")
    List<Consulta> findAllWithPacienteAndMedico();

    /**
     * Busca uma consulta por ID com os dados de paciente e médico já carregados.
     */
    @Query("SELECT c FROM Consulta c LEFT JOIN FETCH c.paciente LEFT JOIN FETCH c.medico WHERE c.id = :id")
    Optional<Consulta> findByIdWithPacienteAndMedico(@Param("id") Long id);

    /**
     * Busca todas as consultas de um determinado médico.
     */
    List<Consulta> findByMedicoId(Long medicoId);
    List<Consulta> findByPacienteId(Long pacienteId);

    /**
     * Busca consultas por data específica usando HQL.
     *
     * @param data A data exata das consultas a serem buscadas
     * @return Lista de consultas na data especificada
     */
    @Query("SELECT c FROM Consulta c WHERE DATE(c.data) = :data")
    List<Consulta> findByData(@Param("data") LocalDate data);

    /**
     * Busca consultas em um intervalo de datas usando HQL.
     *
     * @param dataInicio A data inicial do intervalo
     * @param dataFim A data final do intervalo
     * @return Lista de consultas no intervalo de datas especificado
     */
    @Query("SELECT c FROM Consulta c WHERE c.data BETWEEN :dataInicio AND :dataFim")
    List<Consulta> findByDataBetween(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

    /**
     * Busca consultas por data específica com dados de paciente e médico carregados.
     *
     * @param data A data exata das consultas a serem buscadas
     * @return Lista de consultas na data especificada com dados relacionados
     */
    @Query("SELECT c FROM Consulta c JOIN FETCH c.paciente JOIN FETCH c.medico WHERE DATE(c.data) = :data")
    List<Consulta> findByDataWithPacienteAndMedico(@Param("data") LocalDate data);

    /**
     * Busca consultas a partir de uma data específica, ordenadas cronologicamente.
     *
     * @param data A data a partir da qual buscar consultas
     * @return Lista de consultas ordenadas por data
     */
    @Query("SELECT c FROM Consulta c WHERE c.data >= :data ORDER BY c.data ASC")
    List<Consulta> findByDataAfterOrderByDataAsc(@Param("data") LocalDateTime data);
}
