package br.com.motiva.service;

import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.model.TrechoRodovia;

public class ItemRelatorioPrioridade {
    private TrechoRodovia trecho;
    private IntervencaoOperacional intervencao;
    private String prioridade;
    private String motivo;

    public ItemRelatorioPrioridade(TrechoRodovia trecho, IntervencaoOperacional intervencao, String prioridade,
            String motivo) {
        this.trecho = trecho;
        this.intervencao = intervencao;
        this.prioridade = prioridade;
        this.motivo = motivo;
    }

    public TrechoRodovia getTrecho() {
        return this.trecho;
    }

    public IntervencaoOperacional getIntervencao() {
        return this.intervencao;
    }

    public String getPrioridade() {
        return this.prioridade;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public boolean precisaIntervencao() {
        return this.intervencao != null;
    }
}
