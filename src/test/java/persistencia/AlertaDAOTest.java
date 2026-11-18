package persistencia;

import dominio.Alerta;
import dominio.NivelAlerta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlertaDAOTest {
    @TempDir
    Path pastaTemporaria;

    @Test
    void recuperaAlertaDepoisDeReabrirBanco() throws Exception {
        String url = "jdbc:h2:file:"
                + pastaTemporaria.resolve("citizentech").toAbsolutePath();
        var banco = new BancoDados(url);
        banco.inicializar();

        var esperado = new Alerta(
                "nivel-01", 42.5, 30.0, NivelAlerta.CRITICO,
                LocalDateTime.of(2026, 11, 16, 20, 0)
        );
        new AlertaDAO(banco).salvar(esperado);

        var daoReaberto = new AlertaDAO(new BancoDados(url));

        assertEquals(List.of(esperado), daoReaberto.listarTodos());
    }
}