package dominio;

import excecao.DadosInvalidosException;

import java.util.List;

public class PrevisaoComparacao extends EstrategiaPrevisao {
    public PrevisaoComparacao(int tamanhoJanela) {
        super(tamanhoJanela);
    }

    @Override
    public double prever(List<Medicao> historico) {
        int janela = tamanhoJanela();
        long minimoDeLeituras = 2L * janela;

        if (historico == null || historico.size() < minimoDeLeituras) {
            throw new DadosInvalidosException(
                    "São necessárias pelo menos " + minimoDeLeituras
                            + " leituras para comparar dois períodos."
            );
        }

        int inicioRecente = historico.size() - janela;
        double somaAnterior = 0.0;
        double somaRecente = 0.0;

        for (int indice = inicioRecente; indice < historico.size(); indice++) {
            somaAnterior += historico.get(indice - janela).valor();
            somaRecente += historico.get(indice).valor();
        }

        double mediaAnterior = somaAnterior / janela;
        double mediaRecente = somaRecente / janela;
        return Math.max(0.0, mediaRecente + (mediaRecente - mediaAnterior));
    }
}