package com.devtestejavagermatech.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.ArrayList;

import com.devtestejavagermatech.controller.UsuarioController;
import com.devtestejavagermatech.model.Usuario;
import com.devtestejavagermatech.util.exception.ErroSistema;

public class CadastroUsuarios extends JFrame {
    private JTextField nomeField;
    private JTextField telefoneField;
    private JTextField emailField;
    private JFormattedTextField cpfField;
    private JPasswordField senhaField;
    private JTable table;
    private DefaultTableModel tableModel;

    private List<Usuario> listaUsuarios = new ArrayList<>();

    UsuarioController usuarioController = new UsuarioController();

    Usuario usuarioSelecionado = new Usuario();

    public CadastroUsuarios() throws ErroSistema {
        initialize();
        readUsuarios();
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
        telefoneField = new JFormattedTextField(usuarioController.createFormatter("(##) #####-####"));
        formPanel.add(telefoneField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("CPF:"));
        cpfField = new JFormattedTextField(usuarioController.createFormatter("###.###.###-##"));
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
                try {
                    createUsuario();
                } catch (ErroSistema e1) {
                    e1.printStackTrace();
                }
                limpaForm();
            }
        });
        buttonPanel.add(addButton);

        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buscarUsuarios();
                } catch (ErroSistema e1) {
                    e1.printStackTrace();
                }
                limpaForm();
            }
        });
        buttonPanel.add(searchButton);

        JButton listarButton = new JButton("Listar");
        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    readUsuarios();
                } catch (ErroSistema e1) {
                    e1.printStackTrace();
                }
                limpaForm();
            }
        });
        buttonPanel.add(listarButton);

        JButton atualizaButton = new JButton("Atualizar");
        atualizaButton.setVisible(false);

        atualizaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizaButton.setVisible(false);
                System.out.println(e);
                try {
                    atualizaUsuario();
                } catch (ErroSistema e1) {
                    e1.printStackTrace();
                }
                limpaForm();
            }
        });
        buttonPanel.add(atualizaButton);

        JButton deleteButton = new JButton("Excluir");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    excluirUsuario();
                } catch (ErroSistema e1) {
                    e1.printStackTrace();
                }
                limpaForm();
            }
        });
        deleteButton.setVisible(false);
        buttonPanel.add(deleteButton);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[] { "ID", "Nome", "Telefone", "Email", "CPF" }, 0);

        table = new JTable(tableModel);

        table.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    deleteButton.setVisible(true);
                    atualizaUsuarioForm(listaUsuarios.get(row));
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void readUsuarios() throws ErroSistema {

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

    private void createUsuario() throws ErroSistema {

        tableModel.setRowCount(0);

        if (validarCampos()) {
            usuarioController.create(new Usuario(nomeField.getText(), telefoneField.getText(),
                    emailField.getText(), cpfField.getText(), new String(senhaField.getPassword())));
            readUsuarios();
        }

    }

    private void atualizaUsuario() throws ErroSistema {

        if (validarCampos()) {

            usuarioController
                    .update(new Usuario(usuarioSelecionado.getId(), nomeField.getText(), telefoneField.getText(),
                            emailField.getText(), cpfField.getText(), new String(senhaField.getPassword())));

            readUsuarios();
        }
    }

    private void excluirUsuario() throws ErroSistema {

        tableModel.setRowCount(0);

        String cpf = cpfField.getText();

        usuarioController.delete(cpf);

        readUsuarios();
    }

    private void buscarUsuarios() throws ErroSistema {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String cpf = cpfField.getText();
        tableModel.setRowCount(0);
        for (Usuario u : usuarioController.buscarByNomeOuEmailOuCpf(nome, email, cpf)) {
            tableModel.addRow(new Object[] {
                    u.getId(),
                    u.getNome(),
                    u.getTelefone(),
                    u.getEmail(),
                    u.getCpf()
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
                }
            }
        });
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
        setText("Edit");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {

    private final JButton button;
    private final List<Usuario> users;
    private Usuario selectedUser;
    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox, List<Usuario> users) {
        super(checkBox);
        this.users = users;

        button = new JButton();
        button.setOpaque(true);
        button.setText("Edit");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped(); // Stop editing and commit changes

            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        selectedUser = users.get(row); // Get the user object for the current row
        button.setText("Edit");
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return button.getText();
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
