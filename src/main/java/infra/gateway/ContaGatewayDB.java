package infra.gateway;

import domain.gateway.ContaGateway;
import domain.model.Cliente;
import domain.model.Conta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContaGatewayDB implements ContaGateway {
    private final Connection connection;

    public ContaGatewayDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Conta findById(String id) {
        Conta conta = null;

        String sql = "SELECT C.ID, CL.CPF, CL.NOME, C.SALDO, C.SALDO_EMPRESTIMO " +
                "FROM CONTA C " +
                "INNER JOIN CLIENTE CL ON C.CPF_CLIENTE = CL.CPF " +
                "WHERE C.ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Cliente cliente = new Cliente(resultSet.getString("NOME"), resultSet.getString("CPF"));
                conta = new Conta(resultSet.getString("ID"), cliente, resultSet.getDouble("SALDO"), resultSet.getDouble("SALDO_EMPRESTIMO"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conta;
    }

    @Override
    public Conta save(Conta conta) {
        String clienteSql = "INSERT INTO CLIENTE (CPF, NOME) VALUES (?, ?)";
        String contaSql = "INSERT INTO CONTA (ID, CPF_CLIENTE, SALDO, SALDO_EMPRESTIMO) VALUES (?, ?, ?, ?)";

        try {
            try (PreparedStatement clienteStmt = connection.prepareStatement(clienteSql)) {
                clienteStmt.setString(1, conta.getCliente().getCpf());
                clienteStmt.setString(2, conta.getCliente().getNome());
                clienteStmt.executeUpdate();
            }
            try (PreparedStatement contaStmt = connection.prepareStatement(contaSql)) {
                contaStmt.setString(1, conta.getId());
                contaStmt.setString(2, conta.getCliente().getCpf());
                contaStmt.setDouble(3, conta.getSaldo());
                contaStmt.setDouble(4, conta.getSaldoDisponivelParaEmprestimo());
                contaStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conta;
    }

    @Override
    public Conta update(Conta conta) {
        String updateContaSql = "UPDATE CONTA SET SALDO = ?, SALDO_EMPRESTIMO = ? WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(updateContaSql)) {
            stmt.setDouble(1, conta.getSaldo());
            stmt.setDouble(2, conta.getSaldoDisponivelParaEmprestimo());
            stmt.setString(3, conta.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conta;
    }
}