package br.com.motiva.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.motiva.db.ConexaoBD;
import br.com.motiva.model.EquipeManutencaoRegistro;

public class EquipeManutencaoDAO {
    private static final String SQL_INSERIR = "INSERT INTO TB_EQUIPE_MANUTENCAO "
            + "(NOME, QUANTIDADE_INTEGRANTES, ESPECIALIDADE) VALUES (?, ?, ?)";
    private static final String SQL_BUSCAR_POR_ID = "SELECT ID_EQUIPE, NOME, QUANTIDADE_INTEGRANTES, ESPECIALIDADE "
            + "FROM TB_EQUIPE_MANUTENCAO WHERE ID_EQUIPE = ?";
    private static final String SQL_LISTAR_TODAS = "SELECT ID_EQUIPE, NOME, QUANTIDADE_INTEGRANTES, ESPECIALIDADE "
            + "FROM TB_EQUIPE_MANUTENCAO ORDER BY ID_EQUIPE";
    private static final String SQL_ATUALIZAR = "UPDATE TB_EQUIPE_MANUTENCAO SET NOME = ?, "
            + "QUANTIDADE_INTEGRANTES = ?, ESPECIALIDADE = ? WHERE ID_EQUIPE = ?";
    private static final String SQL_DELETAR = "DELETE FROM TB_EQUIPE_MANUTENCAO WHERE ID_EQUIPE = ?";

    public EquipeManutencaoDAO() {
    }

    public long inserir(EquipeManutencaoRegistro equipe) throws SQLException {
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_INSERIR, new String[] { "ID_EQUIPE" })) {
            stmt.setString(1, equipe.nome());
            stmt.setInt(2, equipe.quantidadeIntegrantes());
            stmt.setString(3, equipe.especialidade());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("O banco nao retornou o ID da equipe inserida.");
    }

    public EquipeManutencaoRegistro buscarPorId(long id) throws SQLException {
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

    public List<EquipeManutencaoRegistro> listarTodas() throws SQLException {
        List<EquipeManutencaoRegistro> equipes = new ArrayList<EquipeManutencaoRegistro>();
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_LISTAR_TODAS);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                equipes.add(this.criarRegistro(rs));
            }
        }

        return equipes;
    }

    public boolean atualizar(EquipeManutencaoRegistro equipe) throws SQLException {
        Connection conexao = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_ATUALIZAR)) {
            stmt.setString(1, equipe.nome());
            stmt.setInt(2, equipe.quantidadeIntegrantes());
            stmt.setString(3, equipe.especialidade());
            stmt.setLong(4, equipe.id());
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

    private EquipeManutencaoRegistro criarRegistro(ResultSet rs) throws SQLException {
        return new EquipeManutencaoRegistro(rs.getLong("ID_EQUIPE"), rs.getString("NOME"),
                rs.getInt("QUANTIDADE_INTEGRANTES"), rs.getString("ESPECIALIDADE"));
    }
}
