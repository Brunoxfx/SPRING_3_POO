package br.com.motiva.model;

public class Pulverizacao extends IntervencaoOperacional {
    public Pulverizacao() {
        super("Pulverizacao");
    }

    @Override
    public void executarServico(TrechoRodovia trecho) {
        trecho.atualizarAlturaVegetacao(trecho.getAlturaVegetacaoCm() - 5.0);
        System.out.println("Servico executado: pulverizacao em " + trecho.getDescricaoKm() + ".");
    }
}
