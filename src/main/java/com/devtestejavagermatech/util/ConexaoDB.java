package com.devtestejavagermatech.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.devtestejavagermatech.util.exception.ErroSistema;

public class ConexaoDB {

    private static Connection conexao;

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = findVarAmbiente("DATABASE_URL");
    private static final String USERNAME = findVarAmbiente("DATABASE_USERNAME");
    private static final String PASSWORD = findVarAmbiente("DATABASE_PASSWORD");

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("PostgreSQL JDBC Driver not found.", e);
        }
    }

    public static Connection getConexao() throws ClassNotFoundException {
        if (conexao == null) {
            try {
                Class.forName(DRIVER);
                return DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        }
        return conexao;
    }

    private static String findVarAmbiente(String pVarAmbiente) {
        String varAmbiente = pVarAmbiente;
        try {
            varAmbiente = System.getenv(varAmbiente);
            if (varAmbiente.isEmpty())
                throw new NullPointerException();
        } catch (NullPointerException e) {
            try {
                throw new ErroSistema("Variável de ambiente '" + pVarAmbiente + "' não encontrada!", e);
            } catch (ErroSistema e1) {
                e1.printStackTrace();
            }
        }
        return varAmbiente;
    }

    public static void fecharConexao() throws ErroSistema {
        if (conexao != null) {
            try {
                conexao.close();
                conexao = null;
            } catch (SQLException ex) {
                throw new ErroSistema("Erro ao fechar conexão com o banco de dados!", ex);
            }
        }
    }
}
