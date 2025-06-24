package aplicacao;

import framework.FrameworkClass;
import model.Consulta;
import model.Medico;
import model.Teste;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
//        Teste testeInstance = new Teste();
//        testeInstance.setUrlDB("jdbc:mysql://127.0.0.1:3306/ATA2?useSSL=false&serverTimezone=America/Sao_Paulo");
//        testeInstance.setUserDB("root");
//        testeInstance.setPassDB("root");
//
        List<Teste> testes = FrameworkClass.loadAll(Teste.class, "jdbc:mysql://127.0.0.1:3306/ATA2?useSSL=false&serverTimezone=America/Sao_Paulo", "root", "root");
        for (Teste teste : testes) {
            System.out.println(teste.getName() + "-" + teste.getDescription());
        }

        Medico medico = new Medico();
        medico.setUrlDB("jdbc:mysql://127.0.0.1:3306/sistema_consultas?useSSL=false&serverTimezone=America/Sao_Paulo");
        medico.setPassDB("root");
        medico.setUserDB("root");

        medico.setId(0L);
        medico.setNome("teste");
        medico.setEmail("teste@teste");
        medico.setSenha("test");
        medico.setEspecialidade("bacteria");


        medico.save();
    }
}
