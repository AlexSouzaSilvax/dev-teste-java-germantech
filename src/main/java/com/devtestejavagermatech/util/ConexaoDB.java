package com.devtestejavagermatech.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    private static final String URL = findVarAmbiente("DATABASE_URL");
    private static final String USERNAME = findVarAmbiente("DATABASE_USERNAME");
    private static final String PASSWORD = findVarAmbiente("DATABASE_PASSWORD");

    public Connection conexao() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    private static String findVarAmbiente(String pVarAmbiente) {
        String varAmbiente = pVarAmbiente;
        try {
            varAmbiente = System.getenv(varAmbiente);
            if (varAmbiente.isEmpty())
                throw new NullPointerException();
        } catch (NullPointerException e) {
            System.out.println("Variável de ambiente '" + pVarAmbiente + "' não encontrada!");
        }
        return varAmbiente;
    }
}
