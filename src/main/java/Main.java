import dominio.Local;
import dominio.RegistroAlagamento;
import dominio.StatusDrenagem;
import persistencia.RegistroAlagamentoRepositorio;
import servico.MonitoramentoAlagamentoServico;
import dominio.Medicao;
import dominio.Sensor;
import dominio.SensorChuva;
import dominio.SensorNivelAgua;
import dominio.SensorUmidadeSolo;
import excecao.DadosInvalidosException;
import excecao.FonteIndisponivelException;
import dominio.PontoMonitorado;
import dominio.LimiaresAlerta;
import dominio.DadosTerritorio;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        var repositorio = new RegistroAlagamentoRepositorio();
        var local = new Local("Praça Central", 12.0, StatusDrenagem.INSUFICIENTE);
        var horario = LocalDateTime.of(2026, 11, 12, 14, 0);
        var servico = new MonitoramentoAlagamentoServico(repositorio);

        repositorio.salvar(new RegistroAlagamento(local, horario, 0.0));
        repositorio.salvar(new RegistroAlagamento(local, horario.plusHours(1), 15.0));

        System.out.println(new Medicao("nivel-01", horario, 0.0).valor() + " cm");

        try {
            new Medicao("nivel-01", horario, -1.0);
        } catch (DadosInvalidosException erro) {
            System.out.println(erro.getMessage());
        }

        Sensor ativo = new SensorNivelAgua("nivel-01", true);
        List<Sensor> sensores = List.of(
                ativo,
                new SensorChuva("chuva-01", true),
                new SensorUmidadeSolo("umidade-01", true)
        );

        for (Sensor sensor : sensores) {
            Medicao medicao = sensor.ler();
            System.out.println(medicao.sensorId() + " | " + sensor.tipo() + " = " + medicao.valor());
        }

        

        Sensor indisponivel = new SensorNivelAgua("nivel-02", false);
        var ponto = new PontoMonitorado("ponto-01", "Centro", true, local, sensores);
System.out.println(ponto.verificarStatus());

var pontoComFalha = new PontoMonitorado(
        "ponto-02", "Centro", true, local, List.of(ativo, indisponivel)
);
System.out.println(pontoComFalha.verificarStatus());

        try {
            indisponivel.ler();
        } catch (FonteIndisponivelException erro) {
            System.out.println(erro.getMessage());
        }
        System.out.println(new LimiaresAlerta(10.0, 20.0, 30.0));

            try {
    new LimiaresAlerta(20.0, 10.0, 30.0);
        } catch (DadosInvalidosException erro) {
      System.out.println(erro.getMessage());
            }   
        for (var registro : servico.listarObservacoesComAgua()) {
            System.out.println(registro.nivelAguaEmCentimetros() + " cm");
            var dadosTerritorio = new DadosTerritorio("teste-123");
dadosTerritorio.alterarLimiares(
        "nivel-01", new LimiaresAlerta(10.0, 20.0, 30.0), "teste-123"
);

try {
    dadosTerritorio.alterarLimiares(
            "nivel-01", new LimiaresAlerta(15.0, 25.0, 35.0), "codigo-errado"
    );
} catch (DadosInvalidosException erro) {
    System.out.println(erro.getMessage());
}

dadosTerritorio.consultarLimiares("nivel-01").ifPresent(System.out::println);
        }
    }
}
