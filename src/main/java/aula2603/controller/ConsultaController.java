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

import java.util.List;

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
        // Garanta que está retornando a lista de consultas diretamente
        model.addAttribute("consultas", consultaRepository.findAllWithPacienteAndMedico());
        return "consulta/list"; // Verifique se este é o caminho correto do template
    }

    @GetMapping("/nova")
    public String novoForm(Model model) {
        prepararForm(model, new Consulta());
        return "consulta/form";
    }

    // Formulário pré-preenchido para paciente específico
    @GetMapping("/nova/{id}")
    public String novoFormComPaciente(@PathVariable Long id, Model model) {
        Consulta consulta = new Consulta();
        consulta.setPaciente(pacienteRepository.findById(id).orElseThrow());
        prepararForm(model, consulta);
        return "consulta/form";
    }

    // Método auxiliar para evitar duplicação
    private void prepararForm(Model model, Consulta consulta) {
        model.addAttribute("consulta", consulta);
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("medicos", medicoRepository.findAllByOrderByNomeAsc());
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Consulta consulta,
                         @RequestParam Long pacienteId,
                         @RequestParam Long medicoId,
                         RedirectAttributes redirectAttributes) {

        try {
            Paciente paciente = pacienteRepository.findById(pacienteId)
                    .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
            Medico medico = medicoRepository.findById(medicoId)
                    .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));

            consulta.setPaciente(paciente);
            consulta.setMedico(medico);
            consultaRepository.save(consulta);

            redirectAttributes.addFlashAttribute("success", "Consulta agendada com sucesso");
            return "redirect:/pacientes/consultas/" + pacienteId;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao agendar consulta: " + e.getMessage());
            return "redirect:/consultas/nova";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        // Buscar consulta pelo ID
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada para o ID: " + id));

        // Passar dados para o modelo
        model.addAttribute("consulta", consulta); // A consulta com os dados atuais
        model.addAttribute("pacientes", pacienteRepository.findAll()); // Lista de pacientes (para manter a seleção)
        model.addAttribute("medicos", medicoRepository.findAllByOrderByNomeAsc()); // Lista de médicos

        // Adicionar paciente e médico atuais ao modelo
        model.addAttribute("pacienteId", consulta.getPaciente().getId());
        model.addAttribute("medicoId", consulta.getMedico().getId());

        // Retornar para o formulário de edição
        return "consulta/editar";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id,
                            @ModelAttribute Consulta consultaAtualizada,
                            @RequestParam Long pacienteId,
                            @RequestParam Long medicoId,
                            RedirectAttributes redirectAttributes) {

        try {
            Consulta consultaExistente = consultaRepository.findByIdWithPacienteAndMedico(id)
                    .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));
            // Atualizar campos básicos
            consultaExistente.setData(consultaAtualizada.getData());
            consultaExistente.setValor(consultaAtualizada.getValor());
            consultaExistente.setObservacao(consultaAtualizada.getObservacao());
            // Verificar se houve mudança de paciente
            if (!consultaExistente.getPaciente().getId().equals(pacienteId)) {
                Paciente paciente = pacienteRepository.findById(pacienteId)
                        .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
                consultaExistente.setPaciente(paciente);
            }
            // Verificar se houve mudança de médico
            if (!consultaExistente.getMedico().getId().equals(medicoId)) {
                Medico medico = medicoRepository.findById(medicoId)
                        .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));
                consultaExistente.setMedico(medico);
            }
            // Salvar consulta atualizada
            consultaRepository.save(consultaExistente);

            redirectAttributes.addFlashAttribute("success", "Consulta atualizada com sucesso");
            return "redirect:/pacientes/consultas/" + consultaExistente.getPaciente().getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar consulta: " + e.getMessage());
            return "redirect:/consultas/editar/" + id;
        }
    }


    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Consulta consulta = consultaRepository.findByIdWithPacienteAndMedico(id)
                    .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));

            Long pacienteId = consulta.getPaciente().getId();
            consultaRepository.delete(consulta);

            redirectAttributes.addFlashAttribute("success", "Consulta excluída com sucesso");
            return "redirect:/pacientes/consultas/" + pacienteId;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir consulta: " + e.getMessage());
            return "redirect:/consultas";
        }
    }
}