package servico;

import dominio.EstrategiaPrevisao;
import dominio.PrevisaoComparacao;
import dominio.PrevisaoMediaMovel;
import excecao.DadosInvalidosException;

public class FabricaPrevisao {
    public EstrategiaPrevisao criar(String opcao, int tamanhoJanela) {
        return switch (opcao) {
            case "1" -> new PrevisaoMediaMovel(tamanhoJanela);
            case "2" -> new PrevisaoComparacao(tamanhoJanela);
            default -> throw new DadosInvalidosException(
                    "Escolha 1 para média móvel ou 2 para comparação."
            );
        };
    }
}