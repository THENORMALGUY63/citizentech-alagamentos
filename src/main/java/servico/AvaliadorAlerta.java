package servico;

import dominio.Alerta;
import dominio.LimiaresAlerta;
import dominio.NivelAlerta;

import java.time.LocalDateTime;
import java.util.Optional;

public class AvaliadorAlerta {
    public Optional<Alerta> avaliar(
            String sensorId,
            double valorPrevisto,
            LimiaresAlerta limiares,
            LocalDateTime dataHoraEmissao
    ) {
        if (valorPrevisto >= limiares.critico()) {
            return Optional.of(new Alerta(
                    sensorId, valorPrevisto, limiares.critico(),
                    NivelAlerta.CRITICO, dataHoraEmissao
            ));
        }

        if (valorPrevisto >= limiares.alerta()) {
            return Optional.of(new Alerta(
                    sensorId, valorPrevisto, limiares.alerta(),
                    NivelAlerta.ALERTA, dataHoraEmissao
            ));
        }

        if (valorPrevisto >= limiares.atencao()) {
            return Optional.of(new Alerta(
                    sensorId, valorPrevisto, limiares.atencao(),
                    NivelAlerta.ATENCAO, dataHoraEmissao
            ));
        }

        return Optional.empty();
    }
}