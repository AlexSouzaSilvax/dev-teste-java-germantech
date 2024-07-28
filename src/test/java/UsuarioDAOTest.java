import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.devtestejavagermatech.dao.UsuarioDAO;
import com.devtestejavagermatech.model.Usuario;
import com.devtestejavagermatech.util.exception.ErroSistema;

public class UsuarioDAOTest {

    private UsuarioDAO usuarioDAO;

    @Before
    public void setUp() {
        usuarioDAO = new UsuarioDAO();
    }

    @Test
    public void testCreateUsuario() {
        createUsuario();
        List<Usuario> usuarios = usuarioDAO.read();
        Assert.assertEquals(1, usuarios.size());
        Assert.assertEquals("Alex Silva", usuarios.get(0).getNome());
    }

    @Test
    public void testBuscarUsuarioPorCpf() {
        createUsuario();
        Optional<Usuario> retornoUsuario = usuarioDAO.buscarUsuarioPorCpf("123.456.789-00");
        Assert.assertTrue(retornoUsuario.isPresent());
        Assert.assertEquals("Alex Silva", retornoUsuario.get().getNome());
    }

    @Test
    public void testUpdateUsuario() {
        Usuario usuario = createUsuario();
        usuario.setNome("Alex Souza da Silva");
        usuarioDAO.update(usuario);

        Optional<Usuario> updatedUsuario = usuarioDAO.buscarUsuarioPorCpf("123.456.789-00");
        Assert.assertTrue(updatedUsuario.isPresent());
        Assert.assertEquals("João S. Silva", updatedUsuario.get().getNome());
    }

    @Test
    public void testDeleteUsuarioByCpf() {
        createUsuario();
        usuarioDAO.delete("123.456.789-00");
        List<Usuario> usuarios = usuarioDAO.read();
        Assert.assertTrue(usuarios.isEmpty());
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
}
