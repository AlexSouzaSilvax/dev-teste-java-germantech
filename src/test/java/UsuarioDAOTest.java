import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import com.devtestejavagermatech.dao.UsuarioDAO;
import com.devtestejavagermatech.model.Usuario;
import com.devtestejavagermatech.util.exception.ErroSistema;

public class UsuarioDAOTest {

    private UsuarioDAO usuarioDAO;

    @Before
    public void setUp() throws ErroSistema {
        usuarioDAO = new UsuarioDAO();
    }

    @After
    public void tearDown() throws ErroSistema {
        usuarioDAO.delete(usuarioDAO.buscarUsuarioPorCpf("123.456.789-00").getId());
    }

    @Test
    public void testCreateUsuario() throws ErroSistema {
        Usuario usuario = createUsuario();
        deleteUsuarioById(usuario.getId());
        Assert.assertNotNull(usuarioDAO.buscarUsuarioPorId(usuario.getId()));
    }

    @Test
    public void testBuscarUsuarioPorCpf() throws ErroSistema {
        createUsuario();
        Assert.assertNotNull(usuarioDAO.buscarUsuarioPorCpf("123.456.789-00"));
    }

    @Test
    public void testUpdateUsuario() throws ErroSistema {
        Usuario usuario = createUsuario();
        usuario.setNome("Alex Souza da Silva");
        usuarioDAO.update(usuario);

        Assert.assertNotNull(usuarioDAO.buscarUsuarioPorId(usuario.getId()));
    }

    @Test
    public void testDeleteUsuarioById() throws ErroSistema {
        usuarioDAO.delete(UUID.fromString("c02774de-0af3-47e5-8b5b-2b6de1a5a9f7"));
        Assert.assertNotNull(usuarioDAO.buscarUsuarioPorId(UUID.fromString("c02774de-0af3-47e5-8b5b-2b6de1a5a9f7")));

    }

    private Usuario createUsuario() {
        Usuario usuario = new Usuario(UUID.randomUUID(), "Alex Silva", "123456789", "alex.silva@gmail.com",
                "123.456.789-00", "algumasenhasegura");
        try {
            usuarioDAO.create(usuario);
        } catch (ErroSistema e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public void deleteUsuarioById(UUID pId) throws ErroSistema {
        usuarioDAO.delete(pId);
    }
}
