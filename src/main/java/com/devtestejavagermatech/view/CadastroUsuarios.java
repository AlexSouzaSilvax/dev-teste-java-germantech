package com.devtestejavagermatech.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;

import com.devtestejavagermatech.controller.UsuarioController;
import com.devtestejavagermatech.model.Usuario;
import com.devtestejavagermatech.util.exception.ErroSistema;

public class CadastroUsuarios extends JFrame {
    private JTextField nomeField;
    private JFormattedTextField telefoneField;
    private JTextField emailField;
    private JFormattedTextField cpfField;
    private JPasswordField senhaField;
    private JTable table;
    private DefaultTableModel tableModel;

    JButton novoButton = new JButton("Novo");
    JButton addButton = new JButton("Salvar");
    JButton searchButton = new JButton("Buscar");
    JButton listarButton = new JButton("Listar");
    JButton deleteButton = new JButton("Excluir");

    JButton atualizaButton = new JButton("Atualizar");

    private List<Usuario> listaUsuarios = new ArrayList<>();

    UsuarioController usuarioController = new UsuarioController();

    Usuario usuarioSelecionado = new Usuario();

    public CadastroUsuarios() throws ErroSistema, ClassNotFoundException {
        initialize();
        readUsuarios();
    }

    private void initialize() {
        setTitle("Usuário - Germantech");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(createTableScrollPane(), BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nomeField = new JTextField();
        telefoneField = new JFormattedTextField(usuarioController.createFormatter("(##) #####-####"));
        telefoneField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    telefoneField.setText("");
                    telefoneField.setValue(null);
                });
            }

            @Override
            public void focusLost(FocusEvent e) {
                try {
                    telefoneField.commitEdit();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        emailField = new JTextField();
        cpfField = new JFormattedTextField(usuarioController.createFormatter("###.###.###-##"));
        cpfField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    cpfField.setText("");
                    cpfField.setValue(null);
                });
            }

            @Override
            public void focusLost(FocusEvent e) {
                try {
                    cpfField.commitEdit();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        senhaField = new JPasswordField();

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Telefone:"));
        formPanel.add(telefoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("CPF:"));
        formPanel.add(cpfField);
        formPanel.add(new JLabel("Senha:"));
        formPanel.add(senhaField);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        novoButton.addActionListener(e -> {
            limpaForm();
            deleteButton.setVisible(false);
            atualizaButton.setVisible(false);
            addButton.setVisible(true);
        });
        buttonPanel.add(novoButton);

        addButton.addActionListener(e -> {
            try {
                createUsuario();
            } catch (ErroSistema e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            limpaForm();
        });
        buttonPanel.add(addButton);

        atualizaButton.setVisible(false);
        atualizaButton.addActionListener(e -> {
            try {
                atualizaUsuario();
            } catch (ErroSistema e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            limpaForm();
            atualizaButton.setVisible(false);
        });

        buttonPanel.add(atualizaButton);

        searchButton.addActionListener(e -> {
            try {
                buscarUsuarios();
            } catch (ErroSistema e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            limpaForm();
        });
        buttonPanel.add(searchButton);

        listarButton.addActionListener(e -> {
            try {
                readUsuarios();
            } catch (ErroSistema e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            limpaForm();
        });
        buttonPanel.add(listarButton);

        deleteButton.setVisible(false);
        deleteButton.addActionListener(e -> {
            try {
                excluirUsuario();
            } catch (ErroSistema e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            limpaForm();
        });
        buttonPanel.add(deleteButton);

        return buttonPanel;
    }

    private JScrollPane createTableScrollPane() {
        tableModel = new DefaultTableModel(new String[] { "ID", "Nome", "Telefone", "Email", "CPF" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);

        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    limpaForm();
                    atualizaUsuarioForm(listaUsuarios.get(row));
                    atualizaButton.setVisible(true);
                    deleteButton.setVisible(true);
                    addButton.setVisible(false);
                }
            }
        });

        return new JScrollPane(table);
    }

    private void readUsuarios() throws ErroSistema, ClassNotFoundException {

        tableModel.setRowCount(0);

        for (Usuario u : listaUsuarios = usuarioController.read()) {
            tableModel.addRow(new Object[] {
                    u.getId(),
                    u.getNome(),
                    u.getTelefone(),
                    u.getEmail(),
                    u.getCpf()
            });
        }
    }

    private void createUsuario() throws ErroSistema, ClassNotFoundException {
        if (validarCampos()) {
            usuarioController.create(new Usuario(nomeField.getText(), telefoneField.getText(),
                    emailField.getText(), cpfField.getText(), new String(senhaField.getPassword())));

            tableModel.setRowCount(0);
            limpaForm();
            usuarioSelecionado = new Usuario();
            atualizaUsuarioForm(usuarioSelecionado);
            readUsuarios();
        }

    }

    private void atualizaUsuario() throws ErroSistema, ClassNotFoundException {
        if (validarCampos()) {
            usuarioController
                    .update(new Usuario(usuarioSelecionado.getId(), nomeField.getText(), telefoneField.getText(),
                            emailField.getText(), cpfField.getText(), new String(senhaField.getPassword())));
            tableModel.setRowCount(0);
            limpaForm();
            usuarioSelecionado = new Usuario();
            atualizaUsuarioForm(usuarioSelecionado);
            readUsuarios();
        }
    }

    private void excluirUsuario() throws ErroSistema, ClassNotFoundException {

        String cpf = cpfField.getText();

        usuarioController.delete(cpf);

        readUsuarios();
    }

    private void buscarUsuarios() throws ErroSistema, ClassNotFoundException {
        tableModel.setRowCount(0);
        for (Usuario u : usuarioController.buscarByNomeOuEmailOuCpf(nomeField.getText(), emailField.getText(),
                cpfField.getText())) {

            String tel = u.getTelefone();

            if (tel.isEmpty() || tel.equals(null)) {
                tel = "(  )      -    ";
            }

            String cpf = u.getCpf();

            if (cpf.isEmpty() || cpf.equals(null)) {
                cpf = "   .   .   -  ";
            }

            tableModel.addRow(new Object[] {
                    u.getId(),
                    u.getNome(),
                    tel,
                    u.getEmail(),
                    cpf
            });
        }

    }

    private void atualizaUsuarioForm(Usuario usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
        nomeField.setText(usuarioSelecionado.getNome());
        telefoneField.setText(usuarioSelecionado.getTelefone());
        emailField.setText(usuarioSelecionado.getEmail());
        cpfField.setText(usuarioSelecionado.getCpf());
        senhaField.setText(usuarioSelecionado.getSenha());

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

        if (!usuarioController.isValidCPF(cpf)) {
            JOptionPane.showMessageDialog(this, "CPF inválido!");
            return false;
        }

        if (!telefone.equals("(  )      -    ") && usuarioController.verificaNTelefone(telefone)) {
            if (!usuarioController.isValidTelefone(telefone)) {
                JOptionPane.showMessageDialog(this, "Telefone inválido!");
                return false;
            }
        } else {
            telefoneField.setText("");
        }

        if (!email.isEmpty() || !email.equals("")) {
            if (!usuarioController.isValidEmail(email)) {
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

    private void limpaForm() {
        nomeField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        cpfField.setText("");
        senhaField.setText("");

        telefoneField.setValue(null);
        cpfField.setValue(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CadastroUsuarios c;
                try {
                    c = new CadastroUsuarios();
                    c.setVisible(true);
                    c.setLocationRelativeTo(null);
                } catch (ErroSistema e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}