package domain.model;

import domain.exception.SaldoInvalidoException;

import java.util.Objects;

public class Conta {
    private String id;
    private Cliente cliente;
    private Double saldo;
    private Double saldoDisponivelParaEmprestimo;

    public Conta(String id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.saldo = 0.0;
        this.saldoDisponivelParaEmprestimo = 0.0;
    }

    public Conta(String id, Cliente cliente, Double saldo, Double saldoDisponivelParaEmprestimo) {
        this.id = id;
        this.cliente = cliente;
        this.saldo = saldo;
        this.saldoDisponivelParaEmprestimo = saldoDisponivelParaEmprestimo;
    }

    public void depositar(Double valor) {
        this.saldo += valor;
    }

    public void sacar(Double valor) {
        this.saldo -= valor;
    }

    public void adicionarSaldoParaEmprestimo(Double valor) {
        this.saldoDisponivelParaEmprestimo += valor;
    }

    public void removerSaldoParaEmprestimo(Double valor) throws Exception {
        if (this.saldoDisponivelParaEmprestimo < valor) {
            throw new SaldoInvalidoException("Saldo para emprestimo inferior ao solicitado");
        }

        this.saldoDisponivelParaEmprestimo -= valor;
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getSaldoDisponivelParaEmprestimo() {
        return saldoDisponivelParaEmprestimo;
    }

    public void setSaldoDisponivelParaEmprestimo(Double saldoDisponivelParaEmprestimo) {
        this.saldoDisponivelParaEmprestimo = saldoDisponivelParaEmprestimo;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "id='" + id + '\'' +
                ", CPF=" + cliente.getCpf() +
                ", Nome=" + cliente.getNome() +
                ", saldo=" + saldo +
                ", saldoDisponivelParaEmprestimo=" + saldoDisponivelParaEmprestimo +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Conta conta = (Conta) obj;
        return Objects.equals(id, conta.id);
    }
}