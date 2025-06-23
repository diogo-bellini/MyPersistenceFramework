package aplicacao;

import model.Teste;

public class Main {
    public static void main(String[] args){
        Teste testeInstance = new Teste();
        testeInstance.setUrlDB("jdbc:mysql://127.0.0.1:3306/ATA2?useSSL=false&serverTimezone=America/Sao_Paulo");
        testeInstance.setUserDB("root");
        testeInstance.setPassDB("root");

        testeInstance.setName("Name Example");
        testeInstance.setDescription("Description example");
        testeInstance.setMyBool(true);

        testeInstance.save();
    }
}
