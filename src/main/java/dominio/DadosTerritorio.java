package dominio;

import excecao.DadosInvalidosException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DadosTerritorio {
    private final String codigoAcesso;
    private final Map<String, LimiaresAlerta> limiaresPorSensor = new HashMap<>();

    public DadosTerritorio(String codigoAcesso) {
        if (codigoAcesso == null || codigoAcesso.isBlank()) {
            throw new DadosInvalidosException("Informe o código de acesso.");
        }

        this.codigoAcesso = codigoAcesso;
    }

    public void alterarLimiares(
            String sensorId, LimiaresAlerta limiares, String codigoInformado
    ) {
        if (!codigoAcesso.equals(codigoInformado)) {
            throw new DadosInvalidosException("Código de acesso incorreto.");
        }

        if (sensorId == null || sensorId.isBlank() || limiares == null) {
            throw new DadosInvalidosException("Informe o sensor e seus limiares.");
        }

        limiaresPorSensor.put(sensorId, limiares);
    }

    public Optional<LimiaresAlerta> consultarLimiares(String sensorId) {
        return Optional.ofNullable(limiaresPorSensor.get(sensorId));
    }
}