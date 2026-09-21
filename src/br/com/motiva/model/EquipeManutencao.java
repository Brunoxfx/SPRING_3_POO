package br.com.motiva.model;

public class EquipeManutencao {
    private String nome;
    private int quantidadeIntegrantes;
    private String especialidade;

    public EquipeManutencao(String nome, int quantidadeIntegrantes, String especialidade) {
        this.setNome(nome);
        this.setQuantidadeIntegrantes(quantidadeIntegrantes);
        this.setEspecialidade(especialidade);
    }

    public String getNome() {
        return this.nome;
    }

    public int getQuantidadeIntegrantes() {
        return this.quantidadeIntegrantes;
    }

    public String getEspecialidade() {
        return this.especialidade;
    }

    public String obterResumo() {
        return this.nome + " (" + this.quantidadeIntegrantes + " integrantes, " + this.especialidade + ")";
    }

    private void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da equipe deve ser informado.");
        }
        this.nome = nome;
    }

    private void setQuantidadeIntegrantes(int quantidadeIntegrantes) {
        if (quantidadeIntegrantes <= 0) {
            throw new IllegalArgumentException("A equipe deve ter pelo menos um integrante.");
        }
        this.quantidadeIntegrantes = quantidadeIntegrantes;
    }

    private void setEspecialidade(String especialidade) {
        if (especialidade == null || especialidade.trim().isEmpty()) {
            throw new IllegalArgumentException("A especialidade deve ser informada.");
        }
        this.especialidade = especialidade;
    }
}
