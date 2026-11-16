package dominio;

import excecao.DadosInvalidosException;

import java.time.LocalDateTime;

public record Medicao(String sensorId, LocalDateTime dataHora, double valor) {
    public Medicao {
        if (sensorId == null || sensorId.isBlank()) {
            throw new DadosInvalidosException("Informe o identificador do sensor.");
        }

        if (dataHora == null) {
            throw new DadosInvalidosException("Informe a data e a hora da leitura.");
        }

        if (!Double.isFinite(valor) || valor < 0) {
            throw new DadosInvalidosException(
                    "O valor da leitura deve ser finito e maior ou igual a zero."
            );
        }
    }
}