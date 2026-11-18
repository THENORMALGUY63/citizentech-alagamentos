import dominio.Sensor;
import dominio.SensorChuva;
import dominio.SensorNivelAgua;
import dominio.SensorUmidadeSolo;
import excecao.DadosInvalidosException;
import excecao.FonteIndisponivelException;
import persistencia.AlertaDAO;
import persistencia.BancoDados;
import persistencia.MedicaoDAO;
import dominio.DadosTerritorio;
import dominio.LimiaresAlerta;
import servico.FabricaPrevisao;
import servico.ServicoPrevisao;
import dominio.Local;
import dominio.PontoMonitorado;
import dominio.StatusDrenagem;

import java.time.LocalDateTime;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class SistemaMonitoramento {
    public static void main(String[] args) {
        var banco = new BancoDados("jdbc:h2:file:./dados/citizentech");

        try {
            banco.inicializar();
        } catch (SQLException erro) {
            System.out.println("Não foi possível iniciar o banco: " + erro.getMessage());
            return;
        }

        var alertaDAO = new AlertaDAO(banco);

        var medicaoDAO = new MedicaoDAO(banco);
        var servicoPrevisao = new ServicoPrevisao(alertaDAO);
var fabricaPrevisao = new FabricaPrevisao();

String codigoInicial = "teste-123";
var dadosTerritorio = new DadosTerritorio(codigoInicial);
dadosTerritorio.alterarLimiares(
        "nivel-01", new LimiaresAlerta(10, 20, 30), codigoInicial
);
dadosTerritorio.alterarLimiares(
        "chuva-01", new LimiaresAlerta(15, 30, 45), codigoInicial
);
dadosTerritorio.alterarLimiares(
        "umidade-01", new LimiaresAlerta(50, 70, 90), codigoInicial
);
        List<Sensor> sensores = List.of(
                new SensorNivelAgua("nivel-01", true),
                new SensorChuva("chuva-01", true),
                new SensorUmidadeSolo("umidade-01", true)
        );
var local = new Local("Praça Central", 12.0, StatusDrenagem.INSUFICIENTE);
var ponto = new PontoMonitorado("ponto-01", "Centro", true, local, sensores);
        
    try (var entrada = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1 - Listar alertas");
                System.out.println("2 - Ler sensores e gravar medições");
                System.out.println("3 - Consultar medições por sensor");
                System.out.println("4 - Rodar previsão");
                System.out.println("5 - Verificar sensores");
                System.out.println("6 - Gerenciar dados do território");
                System.out.println("7 - Exibir status completo");
                System.out.println("0 - Sair");
                if (!entrada.hasNextLine()) {
                    return;
                }

                switch (entrada.nextLine().trim()) {
                    case "1" -> listarAlertas(alertaDAO);
                    case "2" -> lerEGravarMedicoes(sensores, medicaoDAO);
                    case "3" -> consultarMedicoes(entrada, medicaoDAO);
                    case "4" -> rodarPrevisao(
        entrada, medicaoDAO, servicoPrevisao, fabricaPrevisao, dadosTerritorio
);
                    case "5" -> verificarSensores(sensores);
                    case "6" -> gerenciarDadosTerritorio(entrada, dadosTerritorio);
                    case "7" -> exibirStatusCompleto(ponto, sensores, dadosTerritorio);
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Opção inválida.");
                }
            }
        }
    }
private static void gerenciarDadosTerritorio(
        Scanner entrada, DadosTerritorio dadosTerritorio
) {
    System.out.print("ID do sensor (nivel-01, chuva-01 ou umidade-01): ");
    if (!entrada.hasNextLine()) {
        return;
    }
    String sensorId = entrada.nextLine().trim();

    var atuais = dadosTerritorio.consultarLimiares(sensorId);
    if (atuais.isEmpty()) {
        System.out.println("Não há limiares configurados para esse sensor.");
        return;
    }

    var limiares = atuais.orElseThrow();
    System.out.printf("Atenção: %.1f | Alerta: %.1f | Crítico: %.1f%n",
            limiares.atencao(), limiares.alerta(), limiares.critico());

    System.out.print("Novos limiares separados por espaço, ou Enter para voltar: ");
    if (!entrada.hasNextLine()) {
        return;
    }
    String linha = entrada.nextLine().trim();
    if (linha.isEmpty()) {
        return;
    }

    String[] valores = linha.split("\\s+");
    if (valores.length != 3) {
        System.out.println("Informe três valores na ordem: atenção, alerta e crítico.");
        return;
    }

    try {
        var novosLimiares = new LimiaresAlerta(
                Double.parseDouble(valores[0].replace(',', '.')),
                Double.parseDouble(valores[1].replace(',', '.')),
                Double.parseDouble(valores[2].replace(',', '.'))
        );

        System.out.print("Código de acesso: ");
        if (!entrada.hasNextLine()) {
            return;
        }

        dadosTerritorio.alterarLimiares(sensorId, novosLimiares, entrada.nextLine());
        System.out.println("Limiares atualizados.");
    } catch (NumberFormatException erro) {
        System.out.println("Digite três valores numéricos, por exemplo: 12 22 32.");
    } catch (DadosInvalidosException erro) {
        System.out.println(erro.getMessage());
    }
}
private static void exibirStatusCompleto(
        PontoMonitorado ponto, List<Sensor> sensores, DadosTerritorio dadosTerritorio
) {
    String estado = ponto.verificarStatus() ? "Operacional" : "Indisponível";
    System.out.printf("%s | %s | %s%n", ponto.nome(), ponto.regiao(), estado);

    var local = ponto.local();
    System.out.printf("Elevação: %.1f m | Drenagem: %s%n",
            local.elevacaoEmMetros(), local.statusDrenagem());

    System.out.println("\nSensores");
    verificarSensores(sensores);

    System.out.println("\nLimiares por sensor");
    for (Sensor sensor : sensores) {
        dadosTerritorio.consultarLimiares(sensor.id()).ifPresentOrElse(
                limiares -> System.out.printf(
                        "%s | Atenção: %.1f | Alerta: %.1f | Crítico: %.1f%n",
                        sensor.id(), limiares.atencao(), limiares.alerta(), limiares.critico()
                ),
                () -> System.out.println(sensor.id() + " | Sem limiares configurados")
        );
    }
}
private static void verificarSensores(List<Sensor> sensores) {
    for (Sensor sensor : sensores) {
        String estado = sensor.verificarFuncionamento()
                ? "Funcionando"
                : "Indisponível";

        System.out.printf("%s | %s | %s%n", sensor.id(), sensor.tipo(), estado);
    }
}
    private static void lerEGravarMedicoes(List<Sensor> sensores, MedicaoDAO medicaoDAO) {
        for (Sensor sensor : sensores) {
            try {
                var medicao = sensor.ler();
                medicaoDAO.salvar(medicao);
                System.out.printf(
                        "%s | %s | leitura gravada: %.1f%n",
                        medicao.sensorId(), sensor.tipo(), medicao.valor()
                );
            } catch (DadosInvalidosException | FonteIndisponivelException erro) {
                System.out.println(sensor.id() + " | " + erro.getMessage());
            } catch (SQLException erro) {
                System.out.println("Falha ao gravar a leitura de " + sensor.id()
                        + ": " + erro.getMessage());
            }
        }
    }
    private static void consultarMedicoes(Scanner entrada, MedicaoDAO medicaoDAO) {
    System.out.print("ID do sensor (nivel-01, chuva-01 ou umidade-01): ");

    if (!entrada.hasNextLine()) {
        return;
    }

    String sensorId = entrada.nextLine().trim();

    try {
        var medicoes = medicaoDAO.buscarUltimasPorSensor(sensorId, 10);

        if (medicoes.isEmpty()) {
            System.out.println("Nenhuma medição encontrada para " + sensorId + ".");
            return;
        }

        for (var medicao : medicoes) {
            System.out.printf("%s | %s | %.1f%n",
                    medicao.dataHora(), medicao.sensorId(), medicao.valor());
        }
    } catch (DadosInvalidosException erro) {
        System.out.println(erro.getMessage());
    } catch (SQLException erro) {
        System.out.println("Não foi possível consultar as medições: " + erro.getMessage());
    }
}
private static void rodarPrevisao(
        Scanner entrada,
        MedicaoDAO medicaoDAO,
        ServicoPrevisao servico,
        FabricaPrevisao fabrica,
        DadosTerritorio dadosTerritorio
) {
    System.out.print("ID do sensor (nivel-01, chuva-01 ou umidade-01): ");
    if (!entrada.hasNextLine()) {
        return;
    }
    String sensorId = entrada.nextLine().trim();

    System.out.print("Estratégia (1 - Média móvel, 2 - Comparação): ");
    if (!entrada.hasNextLine()) {
        return;
    }
    String opcao = entrada.nextLine().trim();

    try {
        int tamanhoJanela = 2;
        var estrategia = fabrica.criar(opcao, tamanhoJanela);
        var limiares = dadosTerritorio.consultarLimiares(sensorId)
                .orElseThrow(() -> new DadosInvalidosException(
                        "Não há limiares configurados para o sensor " + sensorId + "."
                ));

        var historico = medicaoDAO.buscarUltimasPorSensor(sensorId, 2 * tamanhoJanela);
        var resultado = servico.executarPrevisao(
                sensorId, historico, estrategia, limiares, LocalDateTime.now()
        );

        System.out.printf("%s | valor previsto: %.1f%n", sensorId, resultado.valorPrevisto());

        if (resultado.alerta().isPresent()) {
            var alerta = resultado.alerta().orElseThrow();
            System.out.printf("Alerta %s gravado | limiar atingido: %.1f%n",
                    alerta.nivel(), alerta.limiarAtingido());
        } else {
            System.out.println("Previsão abaixo do primeiro limiar.");
        }
    } catch (DadosInvalidosException erro) {
        System.out.println(erro.getMessage());
    } catch (SQLException | IllegalStateException erro) {
        System.out.println("Não foi possível concluir a previsão: " + erro.getMessage());
    }
}
    private static void listarAlertas(AlertaDAO alertaDAO) {
        try {
            var alertas = alertaDAO.listarTodos();

            if (alertas.isEmpty()) {
                System.out.println("Nenhum alerta registrado.");
                return;
            }

            for (var alerta : alertas) {
                System.out.printf(
                        "%s | %s | %s | previsto: %.1f | limiar: %.1f%n",
                        alerta.dataHoraEmissao(), alerta.sensorId(), alerta.nivel(),
                        alerta.valorPrevisto(), alerta.limiarAtingido()
                );
            }
        } catch (SQLException erro) {
            System.out.println("Não foi possível consultar os alertas: " + erro.getMessage());
        }
    }
}
