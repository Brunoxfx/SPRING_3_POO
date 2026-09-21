package br.com.motiva.model;

public record TrechoRodoviaRegistro(Long id, double kmInicial, double kmFinal, double alturaVegetacaoCm,
        String tipoAmbiente, String riscoOperacional, Long equipeId) {

    public TrechoRodovia paraDominio() {
        return new TrechoRodovia(this.kmInicial, this.kmFinal, this.alturaVegetacaoCm, this.tipoAmbiente,
                this.riscoOperacional);
    }
}
