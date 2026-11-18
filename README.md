# CitizenTech — Monitoramento de alagamentos

Projeto de Programação Orientada a Objetos da Global Solution 2º/2026, no recorte de clima local do tema CitizenTech.

O sistema simula leituras de chuva, nível da água e umidade do solo em um ponto monitorado. Ele grava as medições, calcula previsões a partir do histórico e registra um alerta quando o valor previsto atinge um limiar configurado. O público beneficiado são moradores de áreas sujeitas a alagamentos e equipes comunitárias que precisam decidir quais locais acompanhar e onde verificar as condições primeiro.

## Como executar

Pré-requisitos de desenvolvimento

- JDK 17.
- Apache Maven.
- VS Code com o Extension Pack for Java para executar pelo editor.

1. Abra no VS Code a pasta que contém o `pom.xml`.
2. No terminal integrado, rode `mvn -version` e confira se o Maven informa Java 17. Em seguida, execute `mvn clean test`.
3. Abra [SistemaMonitoramento.java](src/main/java/SistemaMonitoramento.java) e clique em **Run** acima do método `main`.

O H2 abre e inicializa o banco automaticamente. Quando o programa é executado na raiz do projeto, o arquivo fica em `dados/citizentech.mv.db`. Execute o programa a partir da mesma pasta para consultar os dados das sessões anteriores.

## Menu

| Opção | O que faz |
| --- | --- |
| 1 | Lista os alertas já gravados. |
| 2 | Faz uma leitura simulada de cada sensor e grava as medições válidas. |
| 3 | Mostra até dez medições recentes do sensor informado, em ordem cronológica. |
| 4 | Calcula uma previsão para o sensor e a estratégia escolhidos. Se um limiar for atingido, grava o alerta. |
| 5 | Mostra o estado de funcionamento de cada sensor. |
| 6 | Consulta e altera os limiares por sensor. O código de acesso de demonstração é `teste-123`. |
| 7 | Mostra o status do ponto monitorado, a elevação, a drenagem, os sensores e seus limiares. |
| 0 | Encerra o programa. |

Para uma primeira demonstração, escolha a opção **2** quatro vezes. Depois, na opção **4**, informe `nivel-01` e escolha **1** para média móvel ou **2** para comparação. Se o sistema emitir um alerta, ele aparecerá na opção **1**. Como as leituras são simuladas, o resultado varia entre execuções.

## Decisões do projeto

- **Domínio.** `PontoMonitorado` herda de `ComponenteTerritorial` e usa a interface `Sensor` para verificar o funcionamento dos equipamentos. As leituras de cada sensor são representadas por `Medicao`. As exceções `DadosInvalidosException` e `FonteIndisponivelException` identificam dados inválidos e falhas de leitura.
- **Persistência.** Usamos JDBC com H2 em arquivo para que medições e alertas sobrevivam ao reinício. Os DAOs usam `PreparedStatement` e fecham os recursos com `try-with-resources`. O histórico da previsão é consultado por ID de sensor e entregue em ordem cronológica.
- **Previsões.** A média móvel calcula a média das duas últimas leituras no menu. A comparação usa duas janelas consecutivas de duas leituras e projeta a diferença entre as médias. Cada estratégia exige o histórico mínimo para seu cálculo.
- **Padrão criacional.** A `FabricaPrevisao` concentra a criação das estratégias. O menu informa a opção e o tamanho da janela. O serviço usa o contrato comum de `EstrategiaPrevisao`, o que evita espalhar a escolha das classes concretas.
- **Alertas.** O serviço compara a previsão com os limiares do próprio sensor, começando pelo maior. Atingir um limiar já conta. O alerta registra o sensor, o horário, o nível, o valor previsto e o limiar atingido. Os valores iniciais são de demonstração. As alterações pela opção 6 valem durante a execução.
- **Testes.** O JUnit cobre as estratégias, as exceções, os limiares e a persistência. O Mockito verifica a chamada de gravação do alerta pelo serviço. Um teste de integração usa um banco temporário para verificar a previsão a partir do histórico e a recuperação do alerta gerado.

## Reflexão crítica

O sistema deve deixar de emitir uma previsão quando não houver leituras suficientes para o cálculo. Ter duas ou quatro leituras permite executar as estratégias atuais, mas ainda oferece pouca evidência para decidir se uma tendência vai continuar. Em uma aplicação real, lacunas entre medições, dados antigos e sensores com falha reduziriam ainda mais a confiança no resultado. Por exemplo, comparar duas janelas de leituras coletadas em intervalos muito diferentes pode sugerir uma tendência que vem apenas da forma de coleta. As leituras simuladas permitem testar o fluxo do programa, e os limiares usados aqui servem para demonstrar o mecanismo de alertas.

Cada sensor descreve as condições no seu ponto de instalação. Uma medição na Praça Central diz pouco sobre uma rua próxima com outra elevação ou outro sistema de drenagem. A simulação também não representa fatores como bueiros obstruídos, chuvas em pontos vizinhos ou alagamentos que começam fora do alcance do sensor.

O alerta ajuda a decidir onde verificar primeiro. Uma equipe comunitária pode conferir a situação no local, consultar outras fontes e avaliar quais medidas fazem sentido. Mostrar o sensor, o valor previsto e o limiar atingido torna o alerta verificável por quem recebe a informação.

## Divisão de tarefas

| Integrante | Contribuição |
| --- | --- |
| Giovanni | Persistência das medições e alertas, consultas ao H2 e testes dos DAOs. |
| Gabriel | Estratégias de previsão, fábrica, regras de alerta e testes de previsão e alerta. |
| Gisleine | Verificação dos sensores, gerenciamento dos limiares e exibição do status completo no menu. |

## Uso de IA

Usamos o ChatGPT e o Codex como apoio para entender o enunciado, planejar as etapas, obter exemplos de código, diagnosticar erros de compilação e preparar o README. Também usamos sugestões de correção pontuais em arquivos do projeto. Conferimos os resultados compilando com Java 17, executando os testes com Maven e testando no terminal os fluxos de leitura, previsão, consulta e alteração de limiares.
