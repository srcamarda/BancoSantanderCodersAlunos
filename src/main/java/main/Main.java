package main;

import domain.gateway.ContaGateway;
import domain.model.Cliente;
import domain.model.Conta;
import domain.usecase.ContaUseCase;
import infra.database.H2Connection;
import infra.gateway.ContaGatewayDB;
import infra.gateway.ContaGatewayHttp;
import infra.gateway.ContaGatewayLocal;

import java.sql.Connection;


public class Main {
    public static void main(String[] args) throws Exception {

//        Connection connection = H2Connection.getConnection();
//        ContaGatewayDB contaGatewayDB = new ContaGatewayDB(connection);
//        ContaGateway contaGateway = new ContaGatewayHttp();
//        ContaUseCase contaUseCase = new ContaUseCase(contaGateway);

//        Cliente cliente = new Cliente("Ana", "111.111.111.11");
//        Conta conta1 = new Conta("1", cliente);
//
//        contaGatewayDB.save(conta1);
//        Conta conta = contaGatewayDB.findById("1");
//
//        if (conta != null) {
//            System.out.println("Conta encontrada:");
//            System.out.println(conta);
//        } else {
//            System.out.println("Conta não encontrada.");
//        }
    }
}