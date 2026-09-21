package br.com.motiva.main;

import java.sql.SQLException;
import java.util.List;

import br.com.motiva.dao.EquipeManutencaoDAO;
import br.com.motiva.dao.IntervencaoOperacionalDAO;
import br.com.motiva.dao.RelatorioPrioridadeDAO;
import br.com.motiva.dao.TrechoRodoviaDAO;
import br.com.motiva.db.ConexaoBD;
import br.com.motiva.model.EquipeManutencaoRegistro;
import br.com.motiva.model.IntervencaoOperacionalRegistro;
import br.com.motiva.model.RelatorioPrioridadeRegistro;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.model.TrechoRodoviaRegistro;
import br.com.motiva.service.GeradorRelatorio;

public class Main {
    public static void main(String[] args) {
        ConexaoBD conexaoBD = ConexaoBD.getInstancia();

        Long equipeTemporariaId = null;
        Long trechoTemporarioId = null;
        Long intervencaoTemporariaId = null;
        Long relatorioTemporarioId = null;

        EquipeManutencaoDAO equipeDAO = new EquipeManutencaoDAO();
        TrechoRodoviaDAO trechoDAO = new TrechoRodoviaDAO();
        IntervencaoOperacionalDAO intervencaoDAO = new IntervencaoOperacionalDAO();
        RelatorioPrioridadeDAO relatorioDAO = new RelatorioPrioridadeDAO();

        try {
            conexaoBD.conectar();
            System.out.println("Conexao com o Oracle realizada com sucesso.");

            System.out.println("\n=== CRUD de Equipe de Manutencao ===");
            EquipeManutencaoRegistro novaEquipe = new EquipeManutencaoRegistro(null, "Equipe D'Agua", 5,
                    "Controle preventivo");
            equipeTemporariaId = equipeDAO.inserir(novaEquipe);
            System.out.println("Inserida: " + equipeDAO.buscarPorId(equipeTemporariaId));

            EquipeManutencaoRegistro equipeAtualizada = new EquipeManutencaoRegistro(equipeTemporariaId,
                    "Equipe D'Agua", 6, "Rocada e controle preventivo");
            System.out.println("Atualizacao executada: " + equipeDAO.atualizar(equipeAtualizada));
            imprimirLista(equipeDAO.listarTodas());

            System.out.println("\n=== CRUD de Trecho de Rodovia ===");
            TrechoRodoviaRegistro novoTrecho = new TrechoRodoviaRegistro(null, 90.0, 91.0, 45.0,
                    TrechoRodovia.AMBIENTE_UMIDO, TrechoRodovia.RISCO_BAIXO, equipeTemporariaId);
            trechoTemporarioId = trechoDAO.inserir(novoTrecho);
            System.out.println("Inserido: " + trechoDAO.buscarPorId(trechoTemporarioId));

            TrechoRodoviaRegistro trechoAtualizado = new TrechoRodoviaRegistro(trechoTemporarioId, 90.0, 91.0,
                    85.0, TrechoRodovia.AMBIENTE_UMIDO, TrechoRodovia.RISCO_ALTO, equipeTemporariaId);
            System.out.println("Atualizacao executada: " + trechoDAO.atualizar(trechoAtualizado));
            imprimirLista(trechoDAO.listarTodas());

            System.out.println("\n=== CRUD de Intervencao Operacional ===");
            IntervencaoOperacionalRegistro novaIntervencao = new IntervencaoOperacionalRegistro(null,
                    "PULVERIZACAO", trechoTemporarioId, equipeTemporariaId);
            intervencaoTemporariaId = intervencaoDAO.inserir(novaIntervencao);
            System.out.println("Inserida: " + intervencaoDAO.buscarPorId(intervencaoTemporariaId));

            IntervencaoOperacionalRegistro intervencaoAtualizada = new IntervencaoOperacionalRegistro(
                    intervencaoTemporariaId, "ROCADA_MANUAL", trechoTemporarioId, equipeTemporariaId);
            System.out.println("Atualizacao executada: " + intervencaoDAO.atualizar(intervencaoAtualizada));
            imprimirLista(intervencaoDAO.listarTodas());

            System.out.println("\n=== CRUD temporario de Relatorio ===");
            RelatorioPrioridadeRegistro relatorioTemporario = new RelatorioPrioridadeRegistro(null, 0, 0, 0, 1,
                    "Registro temporario para demonstrar o CRUD", null);
            relatorioTemporarioId = relatorioDAO.inserir(relatorioTemporario);
            System.out.println("Inserido: " + relatorioDAO.buscarPorId(relatorioTemporarioId));

            RelatorioPrioridadeRegistro relatorioAtualizado = new RelatorioPrioridadeRegistro(
                    relatorioTemporarioId, 1, 0, 0, 0, "Registro temporario atualizado", null);
            System.out.println("Atualizacao executada: " + relatorioDAO.atualizar(relatorioAtualizado));

            System.out.println("\n=== Geracao e persistencia do Relatorio ===");
            List<TrechoRodoviaRegistro> registrosTrechos = trechoDAO.listarTodas();
            TrechoRodovia[] trechos = new TrechoRodovia[registrosTrechos.size()];

            for (int i = 0; i < registrosTrechos.size(); i++) {
                trechos[i] = registrosTrechos.get(i).paraDominio();
            }

            GeradorRelatorio gerador = new GeradorRelatorio();
            gerador.gerarRelatorio(trechos);

            System.out.println("\n=== Historico de Relatorios ===");
            imprimirLista(relatorioDAO.listarTodas());

            System.out.println("Relatorio temporario removido: " + relatorioDAO.deletar(relatorioTemporarioId));
            relatorioTemporarioId = null;
            System.out.println("Intervencao temporaria removida: "
                    + intervencaoDAO.deletar(intervencaoTemporariaId));
            intervencaoTemporariaId = null;
            System.out.println("Trecho temporario removido: " + trechoDAO.deletar(trechoTemporarioId));
            trechoTemporarioId = null;
            System.out.println("Equipe temporaria removida: " + equipeDAO.deletar(equipeTemporariaId));
            equipeTemporariaId = null;

            System.out.println("\nRegistros temporarios removidos. O relatorio gerado foi mantido no historico.");
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            limparRegistrosTemporarios(intervencaoDAO, trechoDAO, equipeDAO, relatorioDAO,
                    intervencaoTemporariaId, trechoTemporarioId, equipeTemporariaId, relatorioTemporarioId);
            conexaoBD.desconectar();
        }
    }

    private static void imprimirLista(List<?> registros) {
        for (int i = 0; i < registros.size(); i++) {
            System.out.println(registros.get(i));
        }
    }

    private static void limparRegistrosTemporarios(IntervencaoOperacionalDAO intervencaoDAO,
            TrechoRodoviaDAO trechoDAO, EquipeManutencaoDAO equipeDAO, RelatorioPrioridadeDAO relatorioDAO,
            Long intervencaoId, Long trechoId, Long equipeId, Long relatorioId) {
        try {
            if (relatorioId != null) {
                relatorioDAO.deletar(relatorioId);
            }
            if (intervencaoId != null) {
                intervencaoDAO.deletar(intervencaoId);
            }
            if (trechoId != null) {
                trechoDAO.deletar(trechoId);
            }
            if (equipeId != null) {
                equipeDAO.deletar(equipeId);
            }
        } catch (SQLException e) {
            System.err.println("Nao foi possivel remover todos os registros temporarios: " + e.getMessage());
        }
    }
}
