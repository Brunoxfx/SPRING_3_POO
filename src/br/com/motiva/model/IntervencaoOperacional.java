package br.com.motiva.model;

public abstract class IntervencaoOperacional {
    private String nome;

    public IntervencaoOperacional(String nome) {
        this.setNome(nome);
    }

    public String getNome() {
        return this.nome;
    }

    public abstract void executarServico(TrechoRodovia trecho);

    private void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da intervencao deve ser informado.");
        }
        this.nome = nome;
    }
}
