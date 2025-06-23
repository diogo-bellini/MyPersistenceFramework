package aplicacao;

import model.Teste;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Teste testeInstance = new Teste();
        testeInstance.setUrlDB("jdbc:mysql://127.0.0.1:3306/ATA2?useSSL=false&serverTimezone=America/Sao_Paulo");
        testeInstance.setUserDB("root");
        testeInstance.setPassDB("root");
        testeInstance.setId(3L);
        testeInstance.setName("ATA2");
        testeInstance.setDescription("ATA2");

        testeInstance.save();

        System.out.println(testeInstance.verifyExistence());
    }
}
