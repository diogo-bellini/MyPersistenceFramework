package aplicacao;

import framework.FrameworkClass;
import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Instanciando o framework com as credenciais
        FrameworkClass framework = new FrameworkClass(
                "jdbc:mysql://127.0.0.1:3306/sistema_consultas?useSSL=false&serverTimezone=America/Sao_Paulo",
                "root",
                "root"
        );

        // ----- Inserir Médico -----
        Medico medico = new Medico();
        medico.setNome("João Cardoso");
        medico.setEmail("joao@clinica.com");
        medico.setSenha("123456");
        medico.setEspecialidade("Cardiologia");
        framework.save(medico);

        // ----- Inserir Paciente -----
        Paciente paciente = new Paciente();
        paciente.setNome("Maria Souza");
        paciente.setEmail("maria@gmail.com");
        paciente.setSenha("senha123");
        framework.save(paciente);

        // ----- Verificar existência -----
        System.out.println("Médico existe? " + framework.verifyExistence(medico));
        System.out.println("Paciente existe? " + framework.verifyExistence(paciente));

        // ----- Atualizar Médico -----
        medico.setEspecialidade("Neurologia");
        framework.save(medico);

        // ----- Buscar por ID -----
        Medico medicoBuscado = new Medico();
        medicoBuscado.setId(medico.getId());
        if (framework.findById(medicoBuscado)) {
            System.out.println("Médico encontrado por ID: " + medicoBuscado.getNome());
        }

        // ----- Buscar por campo -----
        Medico medicoPorEmail = new Medico();
        framework.find("email", "joao@clinica.com", medicoPorEmail);
        System.out.println("Especialidade do médico buscado por e-mail: " + medicoPorEmail.getEspecialidade());

        // ----- Agendar Consulta -----
        AgendamentoConsulta agendamento = new AgendamentoConsulta();
        agendamento.setData(LocalDate.now());
        agendamento.setHora(LocalTime.of(14, 0));
        agendamento.setStatus("Agendada");
        agendamento.setMedico_id(medico.getId());
        agendamento.setPaciente_id(paciente.getId());
        framework.save(agendamento);

        // ----- Registrar Consulta -----
        Consulta consulta = new Consulta();
        consulta.setData(LocalDate.now());
        consulta.setHora(LocalTime.of(15, 0));
        consulta.setMedico_id(medico.getId());
        consulta.setPaciente_id(paciente.getId());
        framework.save(consulta);

        // ----- Diagnóstico -----
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setDescricao("Infecção de garganta");
        diagnostico.setConsulta_id(consulta.getId());
        framework.save(diagnostico);

        // ----- Prescrição -----
        Prescricao prescricao = new Prescricao();
        prescricao.setData_inicio(LocalDate.now());
        prescricao.setData_fim(LocalDate.now().plusDays(7));
        prescricao.setDosagem("1 comprimido");
        prescricao.setFrequencia("3x ao dia");
        prescricao.setMedicamento("Amoxicilina");
        prescricao.setDiagnostico_id(diagnostico.getId());
        framework.save(prescricao);

        // ----- Listar Médicos -----
        List<Medico> medicos = framework.loadAll(Medico.class);
        System.out.println("\nLista de Médicos:");
        for (Medico m : medicos) {
            System.out.println(m.getId() + " - " + m.getNome() + " - " + m.getEspecialidade());
        }
    }
}
