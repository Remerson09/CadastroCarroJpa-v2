package aula2603.controller;

import aula2603.model.entity.Consulta;
import aula2603.model.entity.Medico;
import aula2603.repository.ConsultaRepository;
import aula2603.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private ConsultaRepository consultaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("medicos", medicoRepository.findAll());
        return "medico/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("medico", new Medico());
        return "medico/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Medico medico) {
        medicoRepository.save(medico);
        return "redirect:/medicos";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));
        model.addAttribute("medico", medico);
        return "medico/form";
    }

    @PostMapping("/editar/{id}")
    public String editarSubmit(@PathVariable Long id, @ModelAttribute Medico medico) {
        medico.setId(id); // Garante que o ID será mantido
        medicoRepository.save(medico);
        return "redirect:/medicos";
    }

    @GetMapping("/consultas/{id}")
    public String consultasMedico(@PathVariable Long id, Model model) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));

        List<Consulta> consultas = consultaRepository.findByMedicoId(id); // Alterado para lista

        model.addAttribute("medico", medico);
        model.addAttribute("consultas", consultas); // Agora é uma lista de consultas
        return "medico/consulta";
    }
    @GetMapping("/excluir/{id}")
    public String excluirMedico(@PathVariable Long id) {
        medicoRepository.deleteById(id); // Exclui apenas o médico com o ID informado
        return "redirect:/medicos"; // Redireciona para a lista de médicos
    }

}