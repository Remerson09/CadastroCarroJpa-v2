package aula2603.controller;

import aula2603.model.entity.Consulta;
import aula2603.model.entity.Medico;
import aula2603.model.entity.Paciente;
import aula2603.repository.ConsultaRepository;
import aula2603.repository.MedicoRepository;
import aula2603.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("consultas", consultaRepository.findAllWithPacienteAndMedico());
        return "consulta/list";
    }

    @GetMapping("/nova")
    public String novo(@RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long medicoId,
            Model model) {

        Consulta consulta = new Consulta();

        if (pacienteId != null) {
            consulta.setPaciente(pacienteRepository.findById(pacienteId)
                    .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado")));
        }

        if (medicoId != null) {
            consulta.setMedico(medicoRepository.findById(medicoId)
                    .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado")));
        }

        prepararForm(model, consulta);
        return "consulta/form";
    }

    private void prepararForm(Model model, Consulta consulta) {
        model.addAttribute("consulta", consulta);
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("medicos", medicoRepository.findAllByOrderByNomeAsc());
    }

    @PostMapping("/salvar")
    public String salvar(Consulta consulta,
                         @RequestParam Long pacienteId,
                         @RequestParam Long medicoId,
                         RedirectAttributes redirect) {

        consulta.setPaciente(pacienteRepository.findById(pacienteId).orElseThrow());
        consulta.setMedico(medicoRepository.findById(medicoId).orElseThrow());

        consultaRepository.save(consulta);
        redirect.addFlashAttribute("success", "Consulta agendada com sucesso");
        return "redirect:/consultas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute(consultaRepository.findByIdWithPacienteAndMedico(id).orElseThrow());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("medicos", medicoRepository.findAllByOrderByNomeAsc());
        return "consulta/editar";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, Consulta consulta,
                            @RequestParam Long pacienteId,
                            @RequestParam Long medicoId,
                            RedirectAttributes redirect) {

        Consulta existente = consultaRepository.findById(id).orElseThrow();
        existente.setData(consulta.getData());
        existente.setValor(consulta.getValor());
        existente.setObservacao(consulta.getObservacao());
        existente.setPaciente(pacienteRepository.findById(pacienteId).orElseThrow());
        existente.setMedico(medicoRepository.findById(medicoId).orElseThrow());

        consultaRepository.save(existente);
        redirect.addFlashAttribute("success", "Consulta atualizada com sucesso");
        return "redirect:/consultas";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        Consulta consulta = consultaRepository.findById(id).orElseThrow();
        consultaRepository.delete(consulta);
        redirect.addFlashAttribute("success", "Consulta excluída com sucesso");
        return "redirect:/consultas";
    }
    @GetMapping("/{id}/consultas")
    public String consultasPorPaciente(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow();
        model.addAttribute("paciente", paciente);
        model.addAttribute("consultas", consultaRepository.findByPacienteId(id));
        return "paciente/consulta";
    }
}