package br.com.motiva.model;

public class TrechoRodovia {
    public static final String AMBIENTE_SECO = "seco";
    public static final String AMBIENTE_UMIDO = "umido";

    public static final String RISCO_BAIXO = "baixo";
    public static final String RISCO_ALTO = "alto";

    private double kmInicial;
    private double kmFinal;
    private double alturaVegetacaoCm;
    private String tipoAmbiente;
    private String riscoOperacional;
    private EquipeManutencao equipeManutencao;

    public TrechoRodovia(double kmInicial, double kmFinal, double alturaVegetacaoCm, String tipoAmbiente,
            String riscoOperacional) {
        this.setKmInicial(kmInicial);
        this.setKmFinal(kmFinal);
        this.setAlturaVegetacaoCm(alturaVegetacaoCm);
        this.setTipoAmbiente(tipoAmbiente);
        this.setRiscoOperacional(riscoOperacional);
    }

    public double getKmInicial() {
        return this.kmInicial;
    }

    public double getKmFinal() {
        return this.kmFinal;
    }

    public double getAlturaVegetacaoCm() {
        return this.alturaVegetacaoCm;
    }

    public String getTipoAmbiente() {
        return this.tipoAmbiente;
    }

    public String getRiscoOperacional() {
        return this.riscoOperacional;
    }

    public EquipeManutencao getEquipeManutencao() {
        return this.equipeManutencao;
    }

    public String getDescricaoKm() {
        return "KM " + this.kmInicial + " ao KM " + this.kmFinal;
    }

    public void associarEquipeManutencao(EquipeManutencao equipeManutencao) {
        if (equipeManutencao == null) {
            throw new IllegalArgumentException("A equipe de manutencao deve ser informada.");
        }
        this.equipeManutencao = equipeManutencao;
    }

    public void atualizarPorMonitoramento() {
        // Trechos sem sensor nao precisam atualizar dados automaticamente.
    }

    public void atualizarAlturaVegetacao(double novaAlturaCm) {
        this.setAlturaVegetacaoCm(novaAlturaCm);
    }

    public void simularCrescimentoPorDias(int dias) {
        if (dias <= 0) {
            System.out.println("Erro: a quantidade de dias deve ser maior que zero.");
            return;
        }

        double crescimento = this.calcularCrescimentoDiarioCm() * dias;
        this.setAlturaVegetacaoCm(this.alturaVegetacaoCm + crescimento);
    }

    public double calcularCrescimentoDiarioCm() {
        if (AMBIENTE_UMIDO.equalsIgnoreCase(this.tipoAmbiente)) {
            return 3.0;
        }
        return 1.2;
    }

    public boolean isRiscoAlto() {
        return RISCO_ALTO.equalsIgnoreCase(this.riscoOperacional);
    }

    private void setKmInicial(double kmInicial) {
        if (kmInicial < 0) {
            throw new IllegalArgumentException("O KM inicial nao pode ser negativo.");
        }
        this.kmInicial = kmInicial;
    }

    private void setKmFinal(double kmFinal) {
        if (kmFinal <= this.kmInicial) {
            throw new IllegalArgumentException("O KM final deve ser maior que o KM inicial.");
        }
        this.kmFinal = kmFinal;
    }

    private void setAlturaVegetacaoCm(double alturaVegetacaoCm) {
        if (alturaVegetacaoCm < 0) {
            throw new IllegalArgumentException("A altura da vegetacao nao pode ser negativa.");
        }
        this.alturaVegetacaoCm = alturaVegetacaoCm;
    }

    private void setTipoAmbiente(String tipoAmbiente) {
        if (!AMBIENTE_SECO.equalsIgnoreCase(tipoAmbiente) && !AMBIENTE_UMIDO.equalsIgnoreCase(tipoAmbiente)) {
            throw new IllegalArgumentException("O ambiente deve ser seco ou umido.");
        }
        this.tipoAmbiente = tipoAmbiente.toLowerCase();
    }

    private void setRiscoOperacional(String riscoOperacional) {
        if (!RISCO_BAIXO.equalsIgnoreCase(riscoOperacional) && !RISCO_ALTO.equalsIgnoreCase(riscoOperacional)) {
            throw new IllegalArgumentException("O risco deve ser baixo ou alto.");
        }
        this.riscoOperacional = riscoOperacional.toLowerCase();
    }
}
