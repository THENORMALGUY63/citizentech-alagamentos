package dominio;

import excecao.DadosInvalidosException;
import excecao.FonteIndisponivelException;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class SensorChuva implements Sensor {
    private final String id;
    private final boolean funcionando;

    public SensorChuva(String id, boolean funcionando) {
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
        return "Chuva (mm na última hora)";
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

        double chuvaEmMilimetros = ThreadLocalRandom.current().nextInt(61);
    return new Medicao(id, LocalDateTime.now(), chuvaEmMilimetros);
    }
}