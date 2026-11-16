package dominio;

import java.time.LocalDateTime;

public record RegistroAlagamento(
        Local local,
        LocalDateTime dataHoraObservacao,
        double nivelAguaEmCentimetros
) {
    public RegistroAlagamento {
    if (local == null) {
        throw new IllegalArgumentException("Informe o local da observação.");
    }

    if (dataHoraObservacao == null) {
        throw new IllegalArgumentException("Informe a data e a hora da observação.");
    }

    if (!Double.isFinite(nivelAguaEmCentimetros) || nivelAguaEmCentimetros < 0) {
        throw new IllegalArgumentException(
                "O nível da água deve ser um número finito maior ou igual a zero."
        );
    }
}
}