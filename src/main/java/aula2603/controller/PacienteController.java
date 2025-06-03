package aula2603.controller;

import aula2603.model.entity.Consulta;
import aula2603.model.entity.Paciente;
import aula2603.repository.ConsultaRepository;
import aula2603.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private ConsultaRepository consultaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "paciente/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "paciente/form";
    }

    // Método para salvar um novo paciente
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Paciente paciente,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {

        try {
            // Verifica se é um novo paciente (ID deve ser nulo)
            if (paciente.getId() != null) {
                throw new IllegalArgumentException("ID deve ser nulo para novo paciente");
            }

            // Salva o paciente
            Paciente savedPaciente = pacienteRepository.save(paciente);

            // Verifica se o ID foi gerado corretamente
            if (savedPaciente.getId() == null) {
                throw new IllegalStateException("Falha ao gerar ID para o paciente");
            }

            redirectAttributes.addFlashAttribute("success", "Paciente cadastrado com sucesso!");
            return "redirect:/pacientes";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("paciente", paciente); // Mantém os dados do formulário
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar: " + e.getMessage());
            return "redirect:/pacientes/novo";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        model.addAttribute("paciente", paciente);
        return "paciente/form";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id,
                            @ModelAttribute Paciente pacienteAtualizado,
                            RedirectAttributes redirectAttributes) {

        try {
            // 1. Busca o paciente existente
            Paciente pacienteBanco = pacienteRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));

            // 2. Atualiza apenas os campos necessários
            pacienteBanco.setNome(pacienteAtualizado.getNome());
            pacienteBanco.setTelefone(pacienteAtualizado.getTelefone());

            // 3. Salva explicitamente
            pacienteRepository.save(pacienteBanco);

            redirectAttributes.addFlashAttribute("success", "Paciente atualizado com sucesso!");
            return "redirect:/pacientes";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar: " + e.getMessage());
            return "redirect:/pacientes/editar/" + id;
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluirPaciente(@PathVariable Long id) {
        // Remove todas as consultas vinculadas
        List<Consulta> consultas = consultaRepository.findByPacienteId(id);
        consultaRepository.deleteAll(consultas);

        // Agora pode remover o paciente
        pacienteRepository.deleteById(id);

        return "redirect:/pacientes";
    }

    @GetMapping("/{id}/consultas")
    public String consultasPaciente(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));

        // Usa o método com JOIN FETCH para garantir que os médicos sejam carregados junto
        List<Consulta> consultas = consultaRepository.findByPacienteIdWithMedico(id);

        model.addAttribute("paciente", paciente);
        model.addAttribute("consultas", consultas);
        return "paciente/consulta"; // Verifique se esse é o nome correto do seu template
    }
}