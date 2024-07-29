package com.devtestejavagermatech.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
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
    private UsuarioController usuarioController = new UsuarioController();
    private Usuario usuarioSelecionado = new Usuario();

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
        emailField = new JTextField();
        cpfField = new JFormattedTextField(usuarioController.createFormatter("###.###.###-##"));
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

        JButton novoButton = new JButton("Novo");
        novoButton.addActionListener(e -> limpaForm());
        buttonPanel.add(novoButton);

        JButton addButton = new JButton("Salvar");
        addButton.addActionListener(e -> {
            try {
                createUsuario();
            } catch (ErroSistema e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            limpaForm();
        });
        buttonPanel.add(addButton);

        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(e -> {
            try {
                buscarUsuarios();
            } catch (ErroSistema e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            limpaForm();
        });
        buttonPanel.add(searchButton);

        JButton listarButton = new JButton("Listar");
        listarButton.addActionListener(e -> {
            try {
                readUsuarios();
            } catch (ErroSistema e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            limpaForm();
        });
        buttonPanel.add(listarButton);

        JButton atualizaButton = new JButton("Atualizar");
        atualizaButton.setVisible(false);
        atualizaButton.addActionListener(e -> {
            try {
                atualizaUsuario();
            } catch (ErroSistema e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            limpaForm();
            atualizaButton.setVisible(false);
        });
        buttonPanel.add(atualizaButton);

        JButton deleteButton = new JButton("Excluir");
        deleteButton.setVisible(false);
        deleteButton.addActionListener(e -> {
            try {
                excluirUsuario();
            } catch (ErroSistema e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            limpaForm();
        });
        buttonPanel.add(deleteButton);

        return buttonPanel;
    }

    private JScrollPane createTableScrollPane() {
        tableModel = new DefaultTableModel(new String[] { "ID", "Nome", "Telefone", "Email", "CPF" }, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);

        table.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    atualizaUsuarioForm(listaUsuarios.get(row));
                }
            }
        });

        return new JScrollPane(table);
    }

    private void readUsuarios() throws ErroSistema, ClassNotFoundException {
        tableModel.setRowCount(0);
        listaUsuarios = usuarioController.read();
        for (Usuario u : listaUsuarios) {
            tableModel.addRow(new Object[] {
                u.getId(), u.getNome(), u.getTelefone(), u.getEmail(), u.getCpf()
            });
        }
    }

    private void createUsuario() throws ErroSistema, ClassNotFoundException {
        if (validarCampos()) {
            usuarioController.create(new Usuario(nomeField.getText(), telefoneField.getText(), emailField.getText(),
                cpfField.getText(), new String(senhaField.getPassword())));
            readUsuarios();
        }
    }

    private void atualizaUsuario() throws ErroSistema, ClassNotFoundException {
        if (validarCampos()) {
            usuarioController.update(new Usuario(usuarioSelecionado.getId(), nomeField.getText(), telefoneField.getText(),
                emailField.getText(), cpfField.getText(), new String(senhaField.getPassword())));
            readUsuarios();
        }
    }

    private void excluirUsuario() throws ErroSistema, ClassNotFoundException {
        usuarioController.delete(cpfField.getText());
        readUsuarios();
    }

    private void buscarUsuarios() throws ErroSistema, ClassNotFoundException {
        tableModel.setRowCount(0);
        for (Usuario u : usuarioController.buscarByNomeOuEmailOuCpf(nomeField.getText(), emailField.getText(), cpfField.getText())) {
            tableModel.addRow(new Object[] {
                u.getId(), u.getNome(), u.getTelefone(), u.getEmail(), u.getCpf()
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

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório!");
            return false;
        }
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CPF é obrigatório!");
            return false;
        }
        if (!usuarioController.isValidCPF(cpf)) {
            JOptionPane.showMessageDialog(this, "CPF inválido!");
            return false;
        }
        if (!telefone.trim().isEmpty() && !usuarioController.isValidTelefone(telefone)) {
            JOptionPane.showMessageDialog(this, "Telefone inválido!");
            return false;
        }
        if (!email.trim().isEmpty() && !usuarioController.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email inválido!");
            return false;
        }
        if (senha.isEmpty()) {
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
        SwingUtilities.invokeLater(() -> {
            try {
                CadastroUsuarios frame = new CadastroUsuarios();
                frame.setVisible(true);
            } catch (ErroSistema e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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

        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        selectedUser = users.get(row);
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
