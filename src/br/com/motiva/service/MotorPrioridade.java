package br.com.motiva.service;

import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.model.Pulverizacao;
import br.com.motiva.model.RocadaManual;
import br.com.motiva.model.RocadaMecanizada;
import br.com.motiva.model.TrechoRodovia;

public class MotorPrioridade {
    public static final String PRIORIDADE_URGENTE = "URGENTE";
    public static final String PRIORIDADE_CRITICO = "CRITICO";
    public static final String PRIORIDADE_ATENCAO = "ATENCAO";
    public static final String PRIORIDADE_NORMAL = "NORMAL";

    public ItemRelatorioPrioridade[] gerarRelatorio(TrechoRodovia[] trechos) {
        ItemRelatorioPrioridade[] itens = new ItemRelatorioPrioridade[trechos.length];

        for (int i = 0; i < trechos.length; i++) {
            TrechoRodovia trecho = trechos[i];
            trecho.atualizarPorMonitoramento();
            itens[i] = this.avaliarTrecho(trecho);
        }

        return itens;
    }

    public ItemRelatorioPrioridade avaliarTrecho(TrechoRodovia trecho) {
        double altura = trecho.getAlturaVegetacaoCm();
        IntervencaoOperacional intervencao;
        String prioridade;
        String motivo;

        if (altura >= 80.0 && trecho.isRiscoAlto()) {
            intervencao = new RocadaManual();
            prioridade = PRIORIDADE_URGENTE;
            motivo = "Vegetacao alta em area com risco operacional alto.";
        } else if (altura >= 80.0) {
            intervencao = new RocadaMecanizada();
            prioridade = PRIORIDADE_CRITICO;
            motivo = "Vegetacao alta em area com risco operacional baixo.";
        } else if (altura >= 40.0) {
            intervencao = new Pulverizacao();
            prioridade = PRIORIDADE_ATENCAO;
            motivo = "Vegetacao media exige controle preventivo.";
        } else {
            intervencao = null;
            prioridade = PRIORIDADE_NORMAL;
            motivo = "Vegetacao baixa. Nao ha prioridade de servico.";
        }

        return new ItemRelatorioPrioridade(trecho, intervencao, prioridade, motivo);
    }
}
