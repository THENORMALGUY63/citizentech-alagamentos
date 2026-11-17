package servico;

import dominio.Alerta;
import dominio.LimiaresAlerta;
import dominio.Medicao;
import dominio.NivelAlerta;
import dominio.PrevisaoMediaMovel;
import org.junit.jupiter.api.Test;
import persistencia.GravadorAlerta;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ServicoPrevisaoTest {
    @Test
    void gravaAlertaCriticoQuandoPrevisaoAtingeOLimiar() {
        var gravador = mock(GravadorAlerta.class);
        var servico = new ServicoPrevisao(gravador);
        var limiares = new LimiaresAlerta(10.0, 20.0, 30.0);
        var horario = LocalDateTime.of(2026, 11, 16, 20, 0);
        var historico = List.of(
                new Medicao("nivel-01", horario.minusHours(2), 20.0),
                new Medicao("nivel-01", horario.minusHours(1), 40.0)
        );
        var esperado = new Alerta("nivel-01", 30.0, 30.0, NivelAlerta.CRITICO, horario);

        var resultado = servico.preverEEmitirAlerta(
                "nivel-01", historico, new PrevisaoMediaMovel(2), limiares, horario
        );

        assertEquals(esperado, resultado.orElseThrow());
        verify(gravador).salvar(esperado);
    }
}