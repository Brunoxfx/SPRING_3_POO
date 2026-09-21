package br.com.motiva.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.com.motiva.db.ConexaoBD;
import br.com.motiva.model.IntervencaoOperacionalRegistro;

public class IntervencaoOperacionalDAO {
    private static final String COLUNAS = "ID_INTERVENCAO, TIPO, ID_TRECHO, ID_EQUIPE";
    private static final String SQL_INSERIR = "INSERT INTO TB_INTERVENCAO_OPERACIONAL "
            + "(TIPO, ID_TRECHO, ID_EQUIPE) VALUES (?, ?, ?)";
    private static final String SQL_BUSCAR_POR_ID = "SELECT " + COLUNAS
            + " FROM TB_INTERVENCAO_OPERACIONAL WHERE ID_INTERVENCAO = ?";
    private static final String SQL_LISTAR_TODAS = "SELECT " + COLUNAS
            + " FROM TB_INTERVENCAO_OPERACIONAL ORDER BY ID_INTERVENCAO";
    private static final String SQL_ATUALIZAR = "UPDATE TB_INTERVENCAO_OPERACIONAL "
            + "SET TIPO = ?, ID_TRECHO = ?, ID_EQUIPE = ? WHERE ID_INTERVENCAO = ?";
    private static final String SQL_DELETAR = "DELETE FROM TB_INTERVENCAO_OPERACIONAL WHERE ID_INTERVENCAO = ?";

    public IntervencaoOperacionalDAO() {
    }

    public long inserir(IntervencaoOperacionalRegistro intervencao) throws SQLException {
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_INSERIR,
                new String[] { "ID_INTERVENCAO" })) {
            this.preencherCampos(stmt, intervencao);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("O banco nao retornou o ID da intervencao inserida.");
    }

    public IntervencaoOperacionalRegistro buscarPorId(long id) throws SQLException {
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

    public List<IntervencaoOperacionalRegistro> listarTodas() throws SQLException {
        List<IntervencaoOperacionalRegistro> intervencoes = new ArrayList<IntervencaoOperacionalRegistro>();
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_LISTAR_TODAS);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                intervencoes.add(this.criarRegistro(rs));
            }
        }

        return intervencoes;
    }

    public boolean atualizar(IntervencaoOperacionalRegistro intervencao) throws SQLException {
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_ATUALIZAR)) {
            this.preencherCampos(stmt, intervencao);
            stmt.setLong(4, intervencao.id());
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

    private void preencherCampos(PreparedStatement stmt, IntervencaoOperacionalRegistro intervencao)
            throws SQLException {
        stmt.setString(1, intervencao.tipo());
        stmt.setLong(2, intervencao.trechoId());

        if (intervencao.equipeId() == null) {
            stmt.setNull(3, Types.NUMERIC);
        } else {
            stmt.setLong(3, intervencao.equipeId());
        }
    }

    private IntervencaoOperacionalRegistro criarRegistro(ResultSet rs) throws SQLException {
        long equipeId = rs.getLong("ID_EQUIPE");
        Long equipeIdOuNull = rs.wasNull() ? null : equipeId;

        return new IntervencaoOperacionalRegistro(rs.getLong("ID_INTERVENCAO"), rs.getString("TIPO"),
                rs.getLong("ID_TRECHO"), equipeIdOuNull);
    }
}
