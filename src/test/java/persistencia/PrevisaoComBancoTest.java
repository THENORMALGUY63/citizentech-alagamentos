package persistencia;

import dominio.Alerta;
import dominio.LimiaresAlerta;
import dominio.Medicao;
import dominio.NivelAlerta;
import dominio.PrevisaoMediaMovel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import servico.ServicoPrevisao;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrevisaoComBancoTest {
    @TempDir
    Path pastaTemporaria;

    @Test
    void geraEGravaAlertaComMedicoesDoBanco() throws Exception {
        String url = "jdbc:h2:file:"
                + pastaTemporaria.resolve("citizentech").toAbsolutePath();
        var banco = new BancoDados(url);
        banco.inicializar();

        var medicoes = new MedicaoDAO(banco);
        var servico = new ServicoPrevisao(new AlertaDAO(banco));
        var horario = LocalDateTime.of(2026, 11, 16, 22, 0);
        var limiares = new LimiaresAlerta(10.0, 20.0, 30.0);

        medicoes.salvar(new Medicao("nivel-01", horario.minusHours(2), 20.0));
        medicoes.salvar(new Medicao("nivel-01", horario.minusHours(1), 40.0));
        var historico = medicoes.buscarUltimasPorSensor("nivel-01", 2);

        var emitido = servico.preverEEmitirAlerta(
                "nivel-01", historico, new PrevisaoMediaMovel(2), limiares, horario
        );

        var esperado = new Alerta("nivel-01", 30.0, 30.0, NivelAlerta.CRITICO, horario);
        assertEquals(esperado, emitido.orElseThrow());
        assertEquals(List.of(esperado), new AlertaDAO(new BancoDados(url)).listarTodos());
    }
}