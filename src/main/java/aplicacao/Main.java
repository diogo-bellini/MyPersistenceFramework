package aplicacao;

import framework.FrameworkClass;
import model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
//        Teste testeInstance = new Teste();
//        testeInstance.setUrlDB("jdbc:mysql://127.0.0.1:3306/ATA2?useSSL=false&serverTimezone=America/Sao_Paulo");
//        testeInstance.setUserDB("root");
//        testeInstance.setPassDB("root");
//

        String a = "jdbc:mysql://127.0.0.1:3306/sistema_consultas?useSSL=false&serverTimezone=America/Sao_Paulo";
        String b = "root";

        FrameworkClass frameworkClassInstance = new FrameworkClass(a, b, b);

        Medico medico = new Medico();
        medico.setEspecialidade("dermatologista");
        String campo = "especialidade";
        frameworkClassInstance.find(campo, medico.getEspecialidade(), medico);

        List<Medico> testes = frameworkClassInstance.loadAll(Medico.class);
        //for (Medico teste : testes) {
        //    System.out.println(teste.getNome() + "-" + teste.getEmail());
        //}

        Paciente paciente = new Paciente();
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setDescricao("teste");
        diagnostico.setConsulta_id(12L);
        frameworkClassInstance.save(diagnostico);


    }
}
