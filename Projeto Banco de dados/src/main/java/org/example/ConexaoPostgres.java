package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoPostgres {

    public static void main(String[] args) {
        Connection conexao = null;

        try {
            // Carregar o driver JDBC
            Class.forName("org.postgresql.Driver");

            // Estabelecer a conexão
            String url = "jdbc:postgresql://localhost:5432/biblioteca";
            String usuario = "postgres";
            String senha = "merg01";

            conexao = DriverManager.getConnection(url, usuario, senha);

            // Exibir mensagem de sucesso se a conexão for bem-sucedida
            System.out.println("Conexão bem-sucedida!");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // Fechar a conexão no bloco finally para garantir que seja fechada, independentemente de exceções
            if (conexao != null) {
                try {
                    conexao.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

