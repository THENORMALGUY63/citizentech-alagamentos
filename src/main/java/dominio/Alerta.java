package dominio;

import java.time.LocalDateTime;

public record Alerta(
        String sensorId,
        double valorPrevisto,
        double limiarAtingido,
        NivelAlerta nivel,
        LocalDateTime dataHoraEmissao
) {
}