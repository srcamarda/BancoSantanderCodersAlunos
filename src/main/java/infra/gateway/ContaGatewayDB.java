package infra.gateway;

import domain.gateway.ContaGateway;
import domain.model.Cliente;
import domain.model.Conta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContaGatewayDB implements ContaGateway {
    private Connection connection;

    public ContaGatewayDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Conta findById(String id) {
        Conta conta = null;
        //String sql = "SELECT * FROM conta WHERE id = ?";
        String sql = "SELECT c.id AS conta_id, c.cpf_cliente, cl.nome AS cliente_nome, cl.cpf AS cliente_cpf " +
                "FROM conta c " +
                "JOIN cliente cl ON c.cpf_cliente = cl.cpf " +
                "WHERE c.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                Cliente cliente = new Cliente(resultSet.getString("cliente_nome"), resultSet.getString("cliente_cpf"));
                conta = new Conta(resultSet.getString("conta_id"),cliente);

            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return conta;
    }

    @Override
    public Conta save(Conta conta) {
        String clienteSql = "INSERT INTO cliente (cpf, nome) VALUES (?, ?)";
        String contaSql = "INSERT INTO conta (id, cpf_cliente) VALUES (?, ?)";

        try {
            try(PreparedStatement clienteStmt = connection.prepareStatement(clienteSql)) {
                clienteStmt.setString(1, conta.getCliente().getCpf());
                clienteStmt.setString(2, conta.getCliente().getNome());
                clienteStmt.executeUpdate();
            }
            try(PreparedStatement contaStmt = connection.prepareStatement(contaSql)) {
                contaStmt.setString(1, conta.getId());
                contaStmt.setString(2, conta.getCliente().getCpf());
                contaStmt.executeUpdate();
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return conta;
    }
}
