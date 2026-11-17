package dominio;

import excecao.DadosInvalidosException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstrategiaPrevisaoTest {
    @Test
    void mediaMovelUsaSomenteAsTresUltimasLeituras() {
        var estrategia = new PrevisaoMediaMovel(3);

        double previsao = estrategia.prever(historico());

        assertEquals(20.0, previsao, 0.001);
    }

    @Test
    void comparacaoProjetaADiferencaEntreDuasJanelas() {
        var estrategia = new PrevisaoComparacao(2);

        double previsao = estrategia.prever(historico());

        assertEquals(42.5, previsao, 0.001);
    }

    @Test
    void comparacaoRejeitaHistoricoInsuficiente() {
        var estrategia = new PrevisaoComparacao(3);
        var leituras = historico();

        assertThrows(DadosInvalidosException.class, () -> estrategia.prever(leituras));
    }

    private List<Medicao> historico() {
        var horario = LocalDateTime.of(2026, 11, 12, 14, 0);

        return List.of(
                new Medicao("nivel-01", horario, 5.0),
                new Medicao("nivel-01", horario.plusHours(1), 10.0),
                new Medicao("nivel-01", horario.plusHours(2), 20.0),
                new Medicao("nivel-01", horario.plusHours(3), 30.0)
        );
    }
}