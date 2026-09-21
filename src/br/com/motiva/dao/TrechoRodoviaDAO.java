package br.com.motiva.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.com.motiva.db.ConexaoBD;
import br.com.motiva.model.TrechoRodoviaRegistro;

public class TrechoRodoviaDAO {
    private static final String COLUNAS = "ID_TRECHO, KM_INICIAL, KM_FINAL, ALTURA_VEGETACAO_CM, "
            + "TIPO_AMBIENTE, RISCO_OPERACIONAL, ID_EQUIPE";
    private static final String SQL_INSERIR = "INSERT INTO TB_TRECHO_RODOVIA "
            + "(KM_INICIAL, KM_FINAL, ALTURA_VEGETACAO_CM, TIPO_AMBIENTE, RISCO_OPERACIONAL, ID_EQUIPE) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_BUSCAR_POR_ID = "SELECT " + COLUNAS
            + " FROM TB_TRECHO_RODOVIA WHERE ID_TRECHO = ?";
    private static final String SQL_LISTAR_TODAS = "SELECT " + COLUNAS
            + " FROM TB_TRECHO_RODOVIA ORDER BY ID_TRECHO";
    private static final String SQL_ATUALIZAR = "UPDATE TB_TRECHO_RODOVIA SET KM_INICIAL = ?, KM_FINAL = ?, "
            + "ALTURA_VEGETACAO_CM = ?, TIPO_AMBIENTE = ?, RISCO_OPERACIONAL = ?, ID_EQUIPE = ? "
            + "WHERE ID_TRECHO = ?";
    private static final String SQL_DELETAR = "DELETE FROM TB_TRECHO_RODOVIA WHERE ID_TRECHO = ?";

    public TrechoRodoviaDAO() {
    }

    public long inserir(TrechoRodoviaRegistro trecho) throws SQLException {
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_INSERIR, new String[] { "ID_TRECHO" })) {
            this.preencherCampos(stmt, trecho);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("O banco nao retornou o ID do trecho inserido.");
    }

    public TrechoRodoviaRegistro buscarPorId(long id) throws SQLException {
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

    public List<TrechoRodoviaRegistro> listarTodas() throws SQLException {
        List<TrechoRodoviaRegistro> trechos = new ArrayList<TrechoRodoviaRegistro>();
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_LISTAR_TODAS);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                trechos.add(this.criarRegistro(rs));
            }
        }

        return trechos;
    }

    public boolean atualizar(TrechoRodoviaRegistro trecho) throws SQLException {
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_ATUALIZAR)) {
            this.preencherCampos(stmt, trecho);
            stmt.setLong(7, trecho.id());
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

    private void preencherCampos(PreparedStatement stmt, TrechoRodoviaRegistro trecho) throws SQLException {
        stmt.setDouble(1, trecho.kmInicial());
        stmt.setDouble(2, trecho.kmFinal());
        stmt.setDouble(3, trecho.alturaVegetacaoCm());
        stmt.setString(4, trecho.tipoAmbiente());
        stmt.setString(5, trecho.riscoOperacional());

        if (trecho.equipeId() == null) {
            stmt.setNull(6, Types.NUMERIC);
        } else {
            stmt.setLong(6, trecho.equipeId());
        }
    }

    private TrechoRodoviaRegistro criarRegistro(ResultSet rs) throws SQLException {
        long equipeId = rs.getLong("ID_EQUIPE");
        Long equipeIdOuNull = rs.wasNull() ? null : equipeId;

        return new TrechoRodoviaRegistro(rs.getLong("ID_TRECHO"), rs.getDouble("KM_INICIAL"),
                rs.getDouble("KM_FINAL"), rs.getDouble("ALTURA_VEGETACAO_CM"),
                rs.getString("TIPO_AMBIENTE"), rs.getString("RISCO_OPERACIONAL"), equipeIdOuNull);
    }
}
