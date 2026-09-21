package br.com.motiva.model;

public class TrechoMonitoradoIoT extends TrechoRodovia implements MonitoravelViaIoT {
    private double ultimaLeituraSensorCm;

    public TrechoMonitoradoIoT(double kmInicial, double kmFinal, double alturaVegetacaoCm, String tipoAmbiente,
            String riscoOperacional, double ultimaLeituraSensorCm) {
        super(kmInicial, kmFinal, alturaVegetacaoCm, tipoAmbiente, riscoOperacional);
        this.setUltimaLeituraSensorCm(ultimaLeituraSensorCm);
    }

    public double getUltimaLeituraSensorCm() {
        return this.ultimaLeituraSensorCm;
    }

    public void atualizarLeituraSensor(double novaLeituraSensorCm) {
        this.setUltimaLeituraSensorCm(novaLeituraSensorCm);
    }

    @Override
    public void atualizarPorMonitoramento() {
        this.transmitirDadosSensor();
    }

    @Override
    public double transmitirDadosSensor() {
        this.atualizarAlturaVegetacao(this.ultimaLeituraSensorCm);
        return this.ultimaLeituraSensorCm;
    }

    private void setUltimaLeituraSensorCm(double ultimaLeituraSensorCm) {
        if (ultimaLeituraSensorCm < 0) {
            throw new IllegalArgumentException("A leitura do sensor nao pode ser negativa.");
        }
        this.ultimaLeituraSensorCm = ultimaLeituraSensorCm;
    }
}
