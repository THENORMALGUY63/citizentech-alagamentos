package dominio;

import excecao.DadosInvalidosException;
import excecao.FonteIndisponivelException;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class SensorNivelAgua implements Sensor {
    private final String id;
    private final boolean funcionando;

    public SensorNivelAgua(String id, boolean funcionando) {
        if (id == null || id.isBlank()) {
            throw new DadosInvalidosException("Informe o identificador do sensor.");
        }

        this.id = id;
        this.funcionando = funcionando;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String tipo() {
        return "Nível da água";
    }

    @Override
    public boolean verificarFuncionamento() {
        return funcionando;
    }

    @Override
    public Medicao ler() {
        if (!verificarFuncionamento()) {
            throw new FonteIndisponivelException("Sensor " + id + " indisponível.");
        }

        double nivelEmCentimetros = ThreadLocalRandom.current().nextInt(51);
        return new Medicao(id, LocalDateTime.now(), nivelEmCentimetros);
    }
}