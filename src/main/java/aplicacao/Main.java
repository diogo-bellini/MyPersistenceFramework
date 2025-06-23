package aplicacao;

import model.Teste;

public class Main {
    public static void main(String[] args){
        Teste testeInstance = new Teste();
        testeInstance.setUrlDB("jdbc:mysql://127.0.0.1:3306/ATA2?useSSL=false&serverTimezone=America/Sao_Paulo");
        testeInstance.setUserDB("root");
        testeInstance.setPassDB("root");

        testeInstance.setId(3L);
//        testeInstance.setName("Name Example 2");
//        testeInstance.setDescription("Description example 2");
//        testeInstance.setMyBool(false);

//        testeInstance.save();
        testeInstance.findById();

        System.out.println(testeInstance.getId());
        System.out.println(testeInstance.getName());
        System.out.println(testeInstance.getDescription());
        System.out.println(testeInstance.getMyBool());

    }
}
