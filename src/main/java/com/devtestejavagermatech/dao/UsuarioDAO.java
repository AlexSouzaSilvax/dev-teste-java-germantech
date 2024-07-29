package com.devtestejavagermatech.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.devtestejavagermatech.model.Usuario;
import com.devtestejavagermatech.util.ConexaoDB;
import com.devtestejavagermatech.util.exception.ErroSistema;

public class UsuarioDAO {

    public void create(Usuario usuario) throws ErroSistema {
        try {
            Connection conexao = ConexaoDB.getConexao();
            PreparedStatement ps;

            ps = conexao.prepareStatement(
                    "insert into usuario_ (id, nome, telefone, email, cpf, senha) VALUES (?,?,?,?,?,?)");

            ps.setObject(1, UUID.randomUUID());
            ps.setString(2, usuario.getNome());
            ps.setString(3, usuario.getTelefone());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getCpf());
            ps.setString(6, usuario.getSenha());
            ps.execute();
            ConexaoDB.fecharConexao();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao tentar salvar!", ex);
        }
    }

    public List<Usuario> read() throws ErroSistema {

        try {

            ResultSet resultSet = ConexaoDB.getConexao().prepareStatement("select * from usuario_").executeQuery();

            List<Usuario> usuarios = new ArrayList<>();

            while (resultSet.next()) {

                usuarios.add(new Usuario(UUID.fromString(resultSet.getObject("id").toString()),
                        resultSet.getString("nome"),
                        resultSet.getString("telefone"), resultSet.getString("email"), resultSet.getString("cpf"),
                        resultSet.getString("senha")));
            }

            ConexaoDB.fecharConexao();

            return usuarios;

        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao listar usuarios!", ex);
        }
    }

    public void update(Usuario usuario) throws ErroSistema {
        try {
            Connection conexao = ConexaoDB.getConexao();
            PreparedStatement ps;

            ps = conexao.prepareStatement(
                    "update usuario_ set nome = ?, telefone = ?, email = ?, cpf = ?, senha = ? where id = ?");

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getTelefone());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getCpf());
            ps.setString(5, usuario.getSenha());
            ps.setObject(6, usuario.getId());

            ps.execute();
            ConexaoDB.fecharConexao();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao tentar atualizar!", ex);
        }
    }

    public void delete(UUID uuid) throws ErroSistema {
        try {
            PreparedStatement ps = ConexaoDB.getConexao().prepareStatement("delete from usuario_ where id = ?");
            ps.setObject(1, uuid);
            ps.execute();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao deletar o usuário!", ex);
        }
    }

    public Usuario buscarUsuarioPorCpf(String pCpf) throws ErroSistema {

        try {

            Connection conexao = ConexaoDB.getConexao();
            PreparedStatement ps = conexao.prepareStatement("select * from usuario_ where cpf = ?");

            ps.setString(1, pCpf);

            ResultSet resultSet = ps.executeQuery();
            Usuario usuario = new Usuario();

            while (resultSet.next()) {

                usuario = new Usuario(UUID.fromString(resultSet.getString("id")), resultSet.getString("nome"),
                        resultSet.getString("telefone"), resultSet.getString("email"),
                        resultSet.getString("cpf"),
                        resultSet.getString("senha"));
            }

            return usuario;
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao buscar usuario pelo cpf!", ex);
        }
    }

    public Usuario buscarUsuarioPorId(UUID pId) throws ErroSistema {

        try {

            Connection conexao = ConexaoDB.getConexao();
            PreparedStatement ps = conexao.prepareStatement("select * from usuario_ where id = ?");

            ps.setObject(1, pId);

            ResultSet resultSet = ps.executeQuery();
            Usuario usuario = new Usuario();

            while (resultSet.next()) {

                usuario = new Usuario(UUID.fromString(resultSet.getString("id")), resultSet.getString("nome"),
                        resultSet.getString("telefone"), resultSet.getString("email"),
                        resultSet.getString("cpf"),
                        resultSet.getString("senha"));
            }

            return usuario;
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao buscar usuario pelo id!", ex);
        }
    }
}
