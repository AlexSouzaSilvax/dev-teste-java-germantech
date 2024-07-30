package com.devtestejavagermatech.controller;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;

import org.mindrot.jbcrypt.BCrypt;

import com.devtestejavagermatech.dao.UsuarioDAO;
import com.devtestejavagermatech.model.Usuario;
import com.devtestejavagermatech.util.ConexaoDB;
import com.devtestejavagermatech.util.exception.ErroSistema;

public class UsuarioController {

    Connection conexao;
    UsuarioDAO usuarioDAO;

    public UsuarioController() throws ClassNotFoundException {
        conexao = ConexaoDB.getConexao();
        usuarioDAO = new UsuarioDAO();
    }

    public void create(Usuario pUsuario) throws ErroSistema, ClassNotFoundException {
        if (existeUsuarioCpf(pUsuario.getCpf())) {
            JOptionPane.showMessageDialog(null, "CPF já cadastrado!", "Usuário", 1);
            return;
        }
        String hashedSenha = BCrypt.hashpw(pUsuario.getSenha(), BCrypt.gensalt());
        pUsuario.setId(UUID.randomUUID());
        pUsuario.setSenha(hashedSenha);
        if (pUsuario.getTelefone().equals("(  )      -    ")) {
            pUsuario.setTelefone(null);
        }
        usuarioDAO.create(pUsuario);
        JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso!", "Usuário", 1);
    }

    public List<Usuario> read() throws ErroSistema, ClassNotFoundException {
        return usuarioDAO.read();
    }

    public void update(Usuario pUsuario) throws ErroSistema, ClassNotFoundException {
        pUsuario.setSenha(BCrypt.hashpw(pUsuario.getSenha(), BCrypt.gensalt()));
        usuarioDAO.update(pUsuario);
        JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso!", "Usuário", 1);
    }

    public void delete(String pCpf) throws ErroSistema, ClassNotFoundException {
        if (pCpf.isEmpty() || pCpf.equals("   .   .   -  ") || pCpf.equals(null)) {
            JOptionPane.showMessageDialog(null, "Selecione um usuário", "Usuário", 1);
            return;
        }
        usuarioDAO.delete(this.buscarUsuarioPorCpf(pCpf).getId());
        JOptionPane.showMessageDialog(null, "Excluído com sucesso!", "Usuário", 1);
    }

    private Usuario buscarUsuarioPorCpf(String pCpf) throws ErroSistema, ClassNotFoundException {
        return usuarioDAO.buscarUsuarioPorCpf(pCpf);
    }

    private boolean existeUsuarioCpf(String pCpf) throws ErroSistema, ClassNotFoundException {
        if (this.buscarUsuarioPorCpf(pCpf).getId() != null) {
            return true;
        }
        return false;
    }

    public List<Usuario> buscarByNomeOuEmailOuCpf(String nome, String email, String cpf)
            throws ErroSistema, ClassNotFoundException {
        return usuarioDAO.buscarByNomeOuEmailOuCpf(nome, email, cpf);
    }

    public boolean verificaNTelefone(String pTelefone) {
        Matcher matcher = Pattern.compile("\\d").matcher(pTelefone);
        return matcher.find();
    }

    public boolean isValidCPF(String cpf) {
        Pattern pattern = Pattern.compile("^(?:\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$");
        Matcher matcher = pattern.matcher(cpf);
        return matcher.matches();
    }

    public boolean isValidTelefone(String telefone) {
        Pattern pattern = Pattern.compile("^\\(\\d{2}\\) \\d{5}-\\d{4}$");
        Matcher matcher = pattern.matcher(telefone);
        return matcher.matches();
    }

    public boolean isValidEmail(String telefone) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(telefone);
        return matcher.matches();
    }

    public MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("Formatter is bad: " + exc.getMessage());
        }
        return formatter;
    }
}
