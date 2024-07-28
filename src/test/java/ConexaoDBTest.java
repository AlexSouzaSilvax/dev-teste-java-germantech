import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.devtestejavagermatech.util.ConexaoDB;

import static org.junit.Assert.*;

import java.sql.Connection;

public class ConexaoDBTest {

    @Before
    public void setUp() {
        /*
         * Banco de dados de teste
         * System.setProperty("DATABASE_URL",
         * "jdbc:postgresql://localhost:5432/seu_banco_de_teste");
         * System.setProperty("DATABASE_USERNAME", "seu_usuario");
         * System.setProperty("DATABASE_PASSWORD", "sua_senha");
         */
    }

    @Test
    public void testGetConexao() {
        try {
            Connection conexao = ConexaoDB.getConexao();
            System.out.println(conexao);
            assertNotNull("Conexão não deve ser nula", conexao);
        } catch (Exception e) {
            fail("Falha ao obter conexão: " + e.getMessage());
        }
    }

    @Test
    public void testFecharConexao() {
        try {
            Connection conexao = ConexaoDB.getConexao();
            assertNotNull("Conexão não deve ser nula", conexao);

            ConexaoDB.fecharConexao();
            assertTrue("Conexão deve ser fechada", conexao.isClosed());
        } catch (Exception e) {
            fail("Falha ao fechar conexão: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        System.clearProperty("DATABASE_URL");
        System.clearProperty("DATABASE_USERNAME");
        System.clearProperty("DATABASE_PASSWORD");
    }
}