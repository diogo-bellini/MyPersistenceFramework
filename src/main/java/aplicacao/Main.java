package aplicacao;

import framework.FrameworkClass;
import model.Teste;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Teste testeInstance = new Teste();
        testeInstance.setUrlDB("jdbc:mysql://127.0.0.1:3306/ATA2?useSSL=false&serverTimezone=America/Sao_Paulo");
        testeInstance.setUserDB("root");
        testeInstance.setPassDB("root");

        List<Teste> testes = FrameworkClass.loadAll(Teste.class, "jdbc:mysql://127.0.0.1:3306/ATA2?useSSL=false&serverTimezone=America/Sao_Paulo", "root", "root");
        for (Teste teste : testes) {
            System.out.println(teste.getName() + "-" + teste.getDescription());
        }

    }
}
