package persistencia;

import dominio.Medicao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MedicaoDAOTest {
    @TempDir
    Path pastaTemporaria;

    @Test
    void recuperaAsUltimasMedicoesDoSensorEmOrdemCronologica() throws Exception {
        var arquivo = pastaTemporaria.resolve("citizentech");
        String url = "jdbc:h2:file:" + arquivo.toAbsolutePath();
        var banco = new BancoDados(url);
        banco.inicializar();
        var dao = new MedicaoDAO(banco);

        var horario = LocalDateTime.of(2026, 11, 16, 14, 0);
        var primeira = new Medicao("nivel-01", horario, 5.0);
        var segunda = new Medicao("nivel-01", horario.plusHours(1), 10.0);
        var terceira = new Medicao("nivel-01", horario.plusHours(2), 20.0);

        dao.salvar(terceira);
        dao.salvar(primeira);
        dao.salvar(segunda);
        dao.salvar(new Medicao("chuva-01", horario.plusHours(3), 45.0));

        var daoReaberto = new MedicaoDAO(new BancoDados(url));
        var resultado = daoReaberto.buscarUltimasPorSensor("nivel-01", 2);

        assertEquals(List.of(segunda, terceira), resultado);
    }
}