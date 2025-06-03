package aula2603.repository;


import aula2603.model.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    List<Medico> findAllByOrderByNomeAsc();
}