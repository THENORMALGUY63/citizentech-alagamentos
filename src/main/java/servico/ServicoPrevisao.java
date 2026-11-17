package servico;

import dominio.Alerta;
import dominio.EstrategiaPrevisao;
import dominio.LimiaresAlerta;
import dominio.Medicao;
import persistencia.GravadorAlerta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ServicoPrevisao {
    private final GravadorAlerta gravador;
    private final AvaliadorAlerta avaliador = new AvaliadorAlerta();

    public ServicoPrevisao(GravadorAlerta gravador) {
        this.gravador = gravador;
    }

    public record ResultadoPrevisao(double valorPrevisto, Optional<Alerta> alerta) {
}

public Optional<Alerta> preverEEmitirAlerta(
        String sensorId,
        List<Medicao> historico,
        EstrategiaPrevisao estrategia,
        LimiaresAlerta limiares,
        LocalDateTime dataHoraEmissao
) {
    return executarPrevisao(
            sensorId, historico, estrategia, limiares, dataHoraEmissao
    ).alerta();
}

public ResultadoPrevisao executarPrevisao(
        String sensorId,
        List<Medicao> historico,
        EstrategiaPrevisao estrategia,
        LimiaresAlerta limiares,
        LocalDateTime dataHoraEmissao
) {
    double valorPrevisto = estrategia.prever(historico);
    var alerta = avaliador.avaliar(sensorId, valorPrevisto, limiares, dataHoraEmissao);
    alerta.ifPresent(gravador::salvar);
    return new ResultadoPrevisao(valorPrevisto, alerta);
}
}