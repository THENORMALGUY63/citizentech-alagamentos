package persistencia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BancoDadosTest {
    @TempDir
    Path pastaTemporaria;

    @Test
    void tabelaDeMedicaoContinuaDisponivelAoReabrirBanco() throws Exception {
        var arquivo = pastaTemporaria.resolve("citizentech");
        var banco = new BancoDados("jdbc:h2:file:" + arquivo.toAbsolutePath());

        banco.inicializar();

        try (var conexao = banco.conectar();
             var comando = conexao.prepareStatement("SELECT COUNT(*) FROM medicao");
             var resultado = comando.executeQuery()) {
            assertTrue(resultado.next());
            assertEquals(0, resultado.getInt(1));
        }
    }
}