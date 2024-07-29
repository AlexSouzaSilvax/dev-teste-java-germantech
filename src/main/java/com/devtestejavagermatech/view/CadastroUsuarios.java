package com.devtestejavagermatech.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import com.devtestejavagermatech.model.Usuario;
import com.devtestejavagermatech.util.ConexaoDB;

public class CadastroUsuarios extends JFrame {
    private JTextField nomeField;
    private JTextField telefoneField;
    private JTextField emailField;
    private JFormattedTextField cpfField;
    private JPasswordField senhaField;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection conexao;

    private List<Usuario> listaUsuarios = new ArrayList<>();

    public CadastroUsuarios() {
        initialize();
        connectToDatabase();
        loadUsuarios();
    }

    private void initialize() {
        setTitle("Usuário - Germantech");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10)); // 5 linhas, 2 colunas, 10px de espaço entre componentes
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Bordas internas

        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("Telefone:"));
        telefoneField = new JFormattedTextField(createFormatter("(##) #####-####"));
        formPanel.add(telefoneField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("CPF:"));
        cpfField = new JFormattedTextField(createFormatter("###.###.###-##"));
        formPanel.add(cpfField);

        formPanel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        formPanel.add(senhaField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Alinhamento dos botões à direita

        JButton novoButton = new JButton("Novo");
        novoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpaForm();
            }
        });
        buttonPanel.add(novoButton);

        JButton addButton = new JButton("Salvar");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarUsuario();
                limpaForm();
            }
        });
        buttonPanel.add(addButton);

        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarUsuarios();
                limpaForm();
            }
        });
        buttonPanel.add(searchButton);

        JButton listarButton = new JButton("Listar");
        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readUsuarios();
                limpaForm();
            }
        });
        buttonPanel.add(listarButton);

        JButton editButton = new JButton("Editar");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e);
                editarUsuario();
                limpaForm();
            }
        });
        buttonPanel.add(editButton);

        JButton deleteButton = new JButton("Excluir");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirUsuario();
                limpaForm();
            }
        });
        buttonPanel.add(deleteButton);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[] { "ID", "Nome", "Telefone", "Email", "CPF" }, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void connectToDatabase() {
        conexao = ConexaoDB.getConexao();
    }

    private void loadUsuarios() {
        tableModel.setRowCount(0);
        try (Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM usuario_")) {
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getObject("id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("cpf")
                });

                listaUsuarios.add(new Usuario(UUID.fromString(rs.getObject("id").toString()), rs.getString("nome"),
                        rs.getString("telefone"), rs.getString("email"), rs.getString("cpf"), rs.getString("senha")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void adicionarUsuario() {
        tableModel.setRowCount(0);
        if (validarCampos()) {
            try {
                String nome = nomeField.getText();
                String telefone = telefoneField.getText();
                String email = emailField.getText();
                String cpf = cpfField.getText();
                String senha = new String(senhaField.getPassword());

                if (usuarioExiste(cpf)) {
                    //implementar update usuario
                    JOptionPane.showMessageDialog(this, "Usuário alterado com sucesso!");
                    return;
                }

                String hashedSenha = BCrypt.hashpw(senha, BCrypt.gensalt());

                PreparedStatement ps = conexao.prepareStatement(
                        "INSERT INTO usuario_ (id, nome, telefone, email, cpf, senha) VALUES (?,?,?,?,?,?)");
                ps.setObject(1, UUID.randomUUID());
                ps.setString(2, nome);
                ps.setString(3, telefone);
                ps.setString(4, email);
                ps.setString(5, cpf);
                ps.setString(6, hashedSenha);
                ps.executeUpdate();
                loadUsuarios();
                JOptionPane.showMessageDialog(this, "Usuário adicionado com sucesso!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void editarUsuario() {

        Usuario usuarioSelecionado = listaUsuarios.get(table.getSelectedRow());
        nomeField.setText(usuarioSelecionado.getNome());
        telefoneField.setText(usuarioSelecionado.getTelefone());
        emailField.setText(usuarioSelecionado.getEmail());
        cpfField.setText(usuarioSelecionado.getCpf());
        senhaField.setText(usuarioSelecionado.getSenha());

        if (validarCampos()) {
            try {
                String nome = nomeField.getText();
                String telefone = telefoneField.getText();
                String email = emailField.getText();
                String cpf = cpfField.getText();
                String senha = new String(senhaField.getPassword());

                if (usuarioExiste(cpf)) {

                    String hashedSenha = BCrypt.hashpw(senha, BCrypt.gensalt());

                    PreparedStatement ps = conexao.prepareStatement(
                            "UPDATE usuario_ SET nome = ?, telefone = ?, email = ?, senha = ? WHERE cpf = ?");
                    ps.setString(1, nome);
                    ps.setString(2, telefone);
                    ps.setString(3, email);
                    ps.setString(4, hashedSenha);
                    ps.setString(5, cpf);
                    ps.executeUpdate();
                    loadUsuarios();
                    JOptionPane.showMessageDialog(this, "Usuário editado com sucesso!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void excluirUsuario() {
        tableModel.setRowCount(0);
        String cpf = cpfField.getText();
        try {
            if (!usuarioExiste(cpf)) {
                JOptionPane.showMessageDialog(this, "CPF não encontrado!");
                loadUsuarios();
                return;
            }

            PreparedStatement ps = conexao.prepareStatement("DELETE FROM usuario_ WHERE cpf = ?");
            ps.setString(1, cpf);
            ps.executeQuery();
            loadUsuarios();
            JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void buscarUsuarios() {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String cpf = cpfField.getText();

        tableModel.setRowCount(0);
        try {
            String query = "SELECT * FROM usuario_ WHERE 1=1";
            if (!nome.isEmpty()) {
                query += " AND nome ILIKE ?";
            }
            if (!email.isEmpty()) {
                query += " AND email ILIKE ?";
            }
            if (!cpf.isEmpty()) {
                query += " AND cpf ILIKE ?";
            }

            PreparedStatement ps = conexao.prepareStatement(query);
            int paramIndex = 1;
            if (!nome.isEmpty()) {
                ps.setString(paramIndex++, "%" + nome + "%");
            }
            if (!email.isEmpty()) {
                ps.setString(paramIndex++, "%" + email + "%");
            }
            if (!cpf.isEmpty()) {
                ps.setString(paramIndex++, "%" + cpf + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getObject("id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("cpf")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readUsuarios() {
        tableModel.setRowCount(0);
        tableModel.setRowCount(0);
        try {
            String query = "SELECT * FROM usuario_";
            PreparedStatement ps = conexao.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getObject("id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("cpf"),
                        rs.getString("senha")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String telefone = telefoneField.getText();
        String email = emailField.getText();
        String senha = new String(senhaField.getPassword());

        if (nome.isEmpty() || nome.equalsIgnoreCase(" ") || nome.equals(null)) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório!");
            return false;
        }

        if (cpf.isEmpty() || cpf.equals("   .   .   -  ") || cpf.equals(null)) {
            JOptionPane.showMessageDialog(this, "CPF é obrigatório!");
            return false;
        }

        if (!isValidCPF(cpf)) {
            JOptionPane.showMessageDialog(this, "CPF inválido!");
            return false;
        }

        if (!telefone.equals("(  )      -    ") && verificaNTelefone(telefone)) {
            if (!isValidTelefone(telefone)) {
                JOptionPane.showMessageDialog(this, "Telefone inválido!");
                return false;
            }
        } else {
            telefoneField.setText("");
        }

        if (!email.isEmpty() || !email.equals("")) {
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Email inválido!");
                return false;
            }
        }

        if (senha.isEmpty() || senha == null) {
            JOptionPane.showMessageDialog(this, "Senha é obrigatória!");
            return false;
        }
        return true;
    }

    private boolean verificaNTelefone(String pTelefone) {
        Matcher matcher = Pattern.compile("\\d").matcher(pTelefone);
        return matcher.find();
    }

    private boolean usuarioExiste(String cpf) throws SQLException {
        PreparedStatement ps = conexao.prepareStatement("SELECT COUNT(*) FROM usuario_ WHERE cpf = ?");
        ps.setString(1, cpf);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    private boolean isValidCPF(String cpf) {
        Pattern pattern = Pattern.compile("^(?:\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$");
        Matcher matcher = pattern.matcher(cpf);
        return matcher.matches();
    }

    private boolean isValidTelefone(String telefone) {
        Pattern pattern = Pattern.compile("^\\(\\d{2}\\) \\d{5}-\\d{4}$");
        Matcher matcher = pattern.matcher(telefone);
        return matcher.matches();
    }

    private boolean isValidEmail(String telefone) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(telefone);
        return matcher.matches();
    }

    private MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("Formatter is bad: " + exc.getMessage());
        }
        return formatter;
    }

    private void limpaForm() {
        nomeField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        cpfField.setText("");
        senhaField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CadastroUsuarios c = new CadastroUsuarios();
                c.setVisible(true);
                c.setLocationRelativeTo(null);
            }
        });
    }
}
