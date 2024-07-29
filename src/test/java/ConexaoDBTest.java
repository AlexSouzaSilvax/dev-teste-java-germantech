import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.devtestejavagermatech.util.ConexaoDB;
import com.devtestejavagermatech.util.exception.ErroSistema;

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
        Connection conexao = ConexaoDB.getConexao();
        assertNotNull(conexao);
    }

    @Test
    public void testFecharConexao() {
        Connection conexao = ConexaoDB.getConexao();
        assertNotNull(conexao);
    }

    @After
    public void tearDown() throws ErroSistema {
        ConexaoDB.fecharConexao();
    }
}