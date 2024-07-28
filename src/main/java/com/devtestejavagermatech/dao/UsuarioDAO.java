package com.devtestejavagermatech.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.devtestejavagermatech.model.Usuario;
import com.devtestejavagermatech.util.ConexaoDB;
import com.devtestejavagermatech.util.exception.ErroSistema;

public class UsuarioDAO {

    public void create(Usuario usuario) throws ErroSistema {
        try {
            Connection conexao = ConexaoDB.getConexao();
            PreparedStatement ps;
            if (usuario.getId() == null) {
                ps = conexao.prepareStatement(
                        "INSERT INTO `usuario_` (`nome`,`telefone`,`email`,`cpf`,`senha`) VALUES (?,?,?,?,?)");
            } else {
                ps = conexao
                        .prepareStatement("update usuario_ set nome=?, telefone=?, email=?, cpf=?, senha=? where id=?");
                ps.setString(6, usuario.getId().toString());
            }
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getTelefone());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getCpf());
            ps.setString(5, usuario.getSenha());
            ps.execute();
            ConexaoDB.fecharConexao();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao tentar salvar!", ex);
        }
    }

    public List<Usuario> read() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listar'");
    }

    public void update(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    public void delete(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    public Optional<Usuario> buscarUsuarioPorCpf(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarUsuarioPorCpf'");
    }

}
