package dominio;

import excecao.DadosInvalidosException;
import excecao.FonteIndisponivelException;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class SensorUmidadeSolo implements Sensor {
    private final String id;
    private final boolean funcionando;

    public SensorUmidadeSolo(String id, boolean funcionando) {
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
        return "Umidade do solo (%)";
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

        double umidadeEmPercentual = ThreadLocalRandom.current().nextInt(101);
        return new Medicao(id, LocalDateTime.now(), umidadeEmPercentual);
    }
}