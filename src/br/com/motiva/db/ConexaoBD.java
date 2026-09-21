package br.com.motiva.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static final String VARIAVEL_URL = "MOTIVA_DB_URL";
    private static final String VARIAVEL_USUARIO = "MOTIVA_DB_USER";
    private static final String VARIAVEL_SENHA = "MOTIVA_DB_PASSWORD";

    private static final ConexaoBD INSTANCIA = new ConexaoBD();

    private Connection conexao;

    private ConexaoBD() {
    }

    public static ConexaoBD getInstancia() {
        return INSTANCIA;
    }

    public synchronized Connection conectar() throws SQLException {
        if (this.conexao != null && !this.conexao.isClosed()) {
            return this.conexao;
        }

        String url = this.lerVariavelObrigatoria(VARIAVEL_URL);
        String usuario = this.lerVariavelObrigatoria(VARIAVEL_USUARIO);
        String senha = this.lerVariavelObrigatoria(VARIAVEL_SENHA);

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver Oracle nao encontrado. Adicione lib/ojdbc17.jar ao classpath.", e);
        }

        this.conexao = DriverManager.getConnection(url, usuario, senha);
        return this.conexao;
    }

    public synchronized void desconectar() {
        if (this.conexao == null) {
            return;
        }

        try {
            if (!this.conexao.isClosed()) {
                this.conexao.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexao: " + e.getMessage());
        } finally {
            this.conexao = null;
        }
    }

    private String lerVariavelObrigatoria(String nome) throws SQLException {
        String valor = System.getenv(nome);
        if (valor == null || valor.trim().isEmpty()) {
            throw new SQLException("Configure a variavel de ambiente " + nome + " antes de executar o sistema.");
        }
        return valor;
    }
}
