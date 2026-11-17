package servico;

import dominio.LimiaresAlerta;
import dominio.NivelAlerta;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AvaliadorAlertaTest {
    @Test
    void emiteAlertaCriticoAoAtingirOLimiar() {
        var avaliador = new AvaliadorAlerta();
        var limiares = new LimiaresAlerta(10.0, 20.0, 30.0);
        var horario = LocalDateTime.of(2026, 11, 16, 20, 0);

        var resultado = avaliador.avaliar("nivel-01", 30.0, limiares, horario);

        var alerta = resultado.orElseThrow();
        assertEquals(NivelAlerta.CRITICO, alerta.nivel());
        assertEquals("nivel-01", alerta.sensorId());
        assertEquals(30.0, alerta.limiarAtingido(), 0.001);
    }

    @Test
    void ficaSemAlertaAbaixoDoPrimeiroLimiar() {
        var avaliador = new AvaliadorAlerta();
        var limiares = new LimiaresAlerta(10.0, 20.0, 30.0);
        var horario = LocalDateTime.of(2026, 11, 16, 20, 0);

        var resultado = avaliador.avaliar("nivel-01", 9.9, limiares, horario);

        assertTrue(resultado.isEmpty());
    }
}