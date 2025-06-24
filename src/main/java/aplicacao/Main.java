package aplicacao;

import framework.FrameworkClass;
import model.Consulta;
import model.Medico;
import model.Teste;

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
        List<Medico> testes = FrameworkClass.loadAll(Medico.class, "jdbc:mysql://127.0.0.1:3306/ATA2?useSSL=false&serverTimezone=America/Sao_Paulo", "root", "root");
        for (Medico teste : testes) {
            System.out.println(teste.getNome() + "-" + teste.getEmail());
        }


        String a = "jdbc:mysql://127.0.0.1:3306/ATA2?useSSL=false&serverTimezone=America/Sao_Paulo";
        String b = "root";

        FrameworkClass frameworkClassInstance = new FrameworkClass(a, b, b);

        Medico medico2 = new Medico();
        medico2.setEspecialidade("dermatologista");
        medico2.setEmail("leo.morikio@gmail.com");
        medico2.setNome("leo");
        medico2.setSenha("1234");
        //frameworkClassInstance.save(medico2);
        Medico medico = new Medico();
        medico.setEspecialidade("dermatologista");
        String campo = "especialidade";
        frameworkClassInstance.find(campo, medico.getEspecialidade(), medico);
        System.out.println(medico.getEmail());



    }
}
