package br.com.motiva.model;

import java.time.LocalDateTime;

public record RelatorioPrioridadeRegistro(Long id, int quantidadeUrgente, int quantidadeCritico,
        int quantidadeAtencao, int quantidadeNormal, String resumo, LocalDateTime dataGeracao) {
}
