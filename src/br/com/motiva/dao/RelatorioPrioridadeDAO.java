package br.com.motiva.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.motiva.db.ConexaoBD;
import br.com.motiva.model.RelatorioPrioridadeRegistro;

public class RelatorioPrioridadeDAO {
    private static final String COLUNAS = "ID_RELATORIO, QUANTIDADE_URGENTE, QUANTIDADE_CRITICO, "
            + "QUANTIDADE_ATENCAO, QUANTIDADE_NORMAL, RESUMO, DATA_GERACAO";
    private static final String SQL_INSERIR = "INSERT INTO TB_RELATORIO_PRIORIDADE "
            + "(QUANTIDADE_URGENTE, QUANTIDADE_CRITICO, QUANTIDADE_ATENCAO, QUANTIDADE_NORMAL, RESUMO) "
            + "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_BUSCAR_POR_ID = "SELECT " + COLUNAS
            + " FROM TB_RELATORIO_PRIORIDADE WHERE ID_RELATORIO = ?";
    private static final String SQL_LISTAR_TODAS = "SELECT " + COLUNAS
            + " FROM TB_RELATORIO_PRIORIDADE ORDER BY ID_RELATORIO";
    private static final String SQL_ATUALIZAR = "UPDATE TB_RELATORIO_PRIORIDADE SET QUANTIDADE_URGENTE = ?, "
            + "QUANTIDADE_CRITICO = ?, QUANTIDADE_ATENCAO = ?, QUANTIDADE_NORMAL = ?, RESUMO = ? "
            + "WHERE ID_RELATORIO = ?";
    private static final String SQL_DELETAR = "DELETE FROM TB_RELATORIO_PRIORIDADE WHERE ID_RELATORIO = ?";

    public RelatorioPrioridadeDAO() {
    }

    public long salvarRelatorio(int quantidadeUrgente, int quantidadeCritico, int quantidadeAtencao,
            int quantidadeNormal, String resumo) throws SQLException {
        RelatorioPrioridadeRegistro relatorio = new RelatorioPrioridadeRegistro(null, quantidadeUrgente,
                quantidadeCritico, quantidadeAtencao, quantidadeNormal, resumo, null);
        return this.inserir(relatorio);
    }

    public long inserir(RelatorioPrioridadeRegistro relatorio) throws SQLException {
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_INSERIR, new String[] { "ID_RELATORIO" })) {
            this.preencherCampos(stmt, relatorio);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("O banco nao retornou o ID do relatorio inserido.");
    }

    public RelatorioPrioridadeRegistro buscarPorId(long id) throws SQLException {
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_BUSCAR_POR_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return this.criarRegistro(rs);
                }
            }
        }

        return null;
    }

    public List<RelatorioPrioridadeRegistro> listarTodas() throws SQLException {
        List<RelatorioPrioridadeRegistro> relatorios = new ArrayList<RelatorioPrioridadeRegistro>();
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_LISTAR_TODAS);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                relatorios.add(this.criarRegistro(rs));
            }
        }

        return relatorios;
    }

    public boolean atualizar(RelatorioPrioridadeRegistro relatorio) throws SQLException {
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_ATUALIZAR)) {
            this.preencherCampos(stmt, relatorio);
            stmt.setLong(6, relatorio.id());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletar(long id) throws SQLException {
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_DELETAR)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private void preencherCampos(PreparedStatement stmt, RelatorioPrioridadeRegistro relatorio) throws SQLException {
        stmt.setInt(1, relatorio.quantidadeUrgente());
        stmt.setInt(2, relatorio.quantidadeCritico());
        stmt.setInt(3, relatorio.quantidadeAtencao());
        stmt.setInt(4, relatorio.quantidadeNormal());
        stmt.setString(5, relatorio.resumo());
    }

    private RelatorioPrioridadeRegistro criarRegistro(ResultSet rs) throws SQLException {
        Timestamp dataGeracao = rs.getTimestamp("DATA_GERACAO");

        return new RelatorioPrioridadeRegistro(rs.getLong("ID_RELATORIO"), rs.getInt("QUANTIDADE_URGENTE"),
                rs.getInt("QUANTIDADE_CRITICO"), rs.getInt("QUANTIDADE_ATENCAO"),
                rs.getInt("QUANTIDADE_NORMAL"), rs.getString("RESUMO"), dataGeracao.toLocalDateTime());
    }
}
