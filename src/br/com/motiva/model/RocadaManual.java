package br.com.motiva.model;

public class RocadaManual extends IntervencaoOperacional {
    public RocadaManual() {
        super("Rocada manual");
    }

    @Override
    public void executarServico(TrechoRodovia trecho) {
        trecho.atualizarAlturaVegetacao(10.0);
        System.out.println("Servico executado: rocada manual em " + trecho.getDescricaoKm() + ".");
    }
}
