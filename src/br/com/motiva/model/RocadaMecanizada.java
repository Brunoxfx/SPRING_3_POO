package br.com.motiva.model;

public class RocadaMecanizada extends IntervencaoOperacional {
    public RocadaMecanizada() {
        super("Rocada mecanizada");
    }

    @Override
    public void executarServico(TrechoRodovia trecho) {
        trecho.atualizarAlturaVegetacao(8.0);
        System.out.println("Servico executado: rocada mecanizada em " + trecho.getDescricaoKm() + ".");
    }
}
