package br.com.motiva.service;

import java.sql.SQLException;

import br.com.motiva.dao.RelatorioPrioridadeDAO;
import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.model.TrechoRodovia;

public class GeradorRelatorio {
    private MotorPrioridade motor;
    private RelatorioPrioridadeDAO relatorioDAO;

    public GeradorRelatorio() {
        this.motor = new MotorPrioridade();
        this.relatorioDAO = new RelatorioPrioridadeDAO();
    }

    public void gerarRelatorio(TrechoRodovia[] trechos) throws SQLException {
        ItemRelatorioPrioridade[] itens = this.motor.gerarRelatorio(trechos);
        this.imprimirRelatorio(itens);

        ResumoPrioridades resumo = this.calcularResumo(itens);
        long idRelatorio = this.relatorioDAO.salvarRelatorio(resumo.quantidadeUrgente(),
                resumo.quantidadeCritico(), resumo.quantidadeAtencao(), resumo.quantidadeNormal(), resumo.texto());

        System.out.println();
        System.out.println("Relatorio salvo no banco com ID " + idRelatorio + ".");
    }

    public ResumoPrioridades calcularResumo(ItemRelatorioPrioridade[] itens) {
        int quantidadeUrgente = 0;
        int quantidadeCritico = 0;
        int quantidadeAtencao = 0;
        int quantidadeNormal = 0;

        for (int i = 0; i < itens.length; i++) {
            String prioridade = itens[i].getPrioridade();

            if (MotorPrioridade.PRIORIDADE_URGENTE.equals(prioridade)) {
                quantidadeUrgente++;
            } else if (MotorPrioridade.PRIORIDADE_CRITICO.equals(prioridade)) {
                quantidadeCritico++;
            } else if (MotorPrioridade.PRIORIDADE_ATENCAO.equals(prioridade)) {
                quantidadeAtencao++;
            } else {
                quantidadeNormal++;
            }
        }

        String texto = "Urgente: " + quantidadeUrgente
                + ", Critico: " + quantidadeCritico
                + ", Atencao: " + quantidadeAtencao
                + ", Normal: " + quantidadeNormal;

        return new ResumoPrioridades(quantidadeUrgente, quantidadeCritico, quantidadeAtencao, quantidadeNormal,
                texto);
    }

    private void imprimirRelatorio(ItemRelatorioPrioridade[] itens) {
        System.out.println("=== Relatorio de Prioridade ===");

        for (int i = 0; i < itens.length; i++) {
            ItemRelatorioPrioridade item = itens[i];
            TrechoRodovia trecho = item.getTrecho();
            IntervencaoOperacional intervencao = item.getIntervencao();

            System.out.println();
            System.out.println("Trecho: " + trecho.getDescricaoKm());
            System.out.println("Ambiente: " + trecho.getTipoAmbiente());
            System.out.println("Risco operacional: " + trecho.getRiscoOperacional());
            System.out.println("Altura da vegetacao: " + trecho.getAlturaVegetacaoCm() + " cm");
            System.out.println("Nivel de prioridade: " + item.getPrioridade());

            if (item.precisaIntervencao()) {
                System.out.println("Intervencao indicada: " + intervencao.getNome());
            } else {
                System.out.println("Intervencao indicada: nenhuma");
            }

            System.out.println("Motivo: " + item.getMotivo());
        }
    }
}
