package usecase;

import domain.gateway.ContaGateway;
import domain.model.Cliente;
import domain.model.Conta;
import domain.usecase.ContaUseCase;
import infra.database.H2Connection;
import infra.gateway.ContaGatewayDB;
import org.junit.*;

import java.sql.Connection;

public class ContaUseCaseTest {

    private static ContaUseCase contaUseCase;
    private static ContaGateway contaGateway;

    @BeforeClass
    public static void beforeClass() {
        Connection connection = H2Connection.getConnection();
        contaGateway = new ContaGatewayDB(connection);
        contaUseCase = new ContaUseCase(contaGateway);

        Cliente cliente1 = new Cliente("Ana", "111.111.111-11");
        Conta conta1 = new Conta("1", cliente1);

        Cliente cliente2 = new Cliente("Carla", "222.222.222-22");
        Conta conta2 = new Conta("2", cliente2);

        contaGateway.save(conta1);
        contaGateway.save(conta2);

        System.out.println("Before class");
    }

    @Before
    public void before() {
        //Reseta contas entre testes
        Conta conta1 = contaGateway.findById("1");
        Conta conta2 = contaGateway.findById("2");

        conta1.setSaldo(0.0);
        conta1.setSaldoDisponivelParaEmprestimo(0.0);

        conta2.setSaldo(0.0);
        conta2.setSaldoDisponivelParaEmprestimo(0.0);

        contaGateway.update(conta1);
        contaGateway.update(conta2);

        System.out.println("before");
    }

    @After
    public void after() {
        System.out.println("After");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("After class");
    }

    @Test
    public void deveTransferirCorretamenteEntreDuasContas() throws Exception {
        System.out.println("deveTransferirCorretamenteEntreDuasContas");

        contaUseCase.depositar("1", 100.0);
        contaUseCase.transferir("1", "2", 20.0);

        Double valorEsperadoConta1 = 80.0;
        Conta conta1 = contaGateway.findById("1");
        Assert.assertEquals(valorEsperadoConta1, conta1.getSaldo());

        Double valorEsperadoConta2 = 20.0;
        Conta conta2 = contaGateway.findById("2");
        Assert.assertEquals(valorEsperadoConta2, conta2.getSaldo());
    }

    @Test
    public void deveDepositarCorretamente() throws Exception {
        System.out.println("deveDepositarCorretamente");

        contaUseCase.depositar("1", 10.0);
        Conta conta1 = contaGateway.findById("1");

        Double valorEsperado = 10.0;
        Assert.assertEquals(valorEsperado, conta1.getSaldo());
    }

    @Test
    public void testeExemplo1() {
        System.out.println("testeExemplo");
    }

    @Test
    public void deveCriarContaCorretamente() {
        System.out.println("deveCriarContaCorretamente");

        Conta conta3 = new Conta("3", new Cliente("Joao", "333.333.333-33"));
        contaUseCase.criarConta(conta3);

        Conta conta3DB = contaGateway.findById(conta3.getId());
        Assert.assertEquals(conta3, conta3DB);
    }

    @Test
    public void deveEncontrarContaCorretamente() {
        System.out.println("deveEncontrarContaCorretamente");

        String id = "1";
        Conta conta1 = contaUseCase.buscarConta(id);
        Assert.assertEquals(id, conta1.getId());
    }

    @Test
    public void deveRealizarEmprestimoCorretamente() throws Exception {
        System.out.println("deveRealizarEmprestimoCorretamente");

        Conta conta1 = contaGateway.findById("1");

        Double valorEmprestimo = 1000.0;
        Double saldoAnterior = conta1.getSaldo();

        conta1.adicionarSaldoParaEmprestimo(valorEmprestimo);
        contaGateway.update(conta1);

        contaUseCase.emprestimo(conta1.getId(), valorEmprestimo);

        Double saldoNovo = contaGateway.findById("1").getSaldo();
        Assert.assertEquals((saldoAnterior + valorEmprestimo), saldoNovo, 0.1);
    }
}