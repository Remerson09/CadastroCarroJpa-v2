package aula2603.repository;

import aula2603.model.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
