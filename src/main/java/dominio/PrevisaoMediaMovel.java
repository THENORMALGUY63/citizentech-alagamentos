package dominio;

import excecao.DadosInvalidosException;

import java.util.List;

public class PrevisaoMediaMovel extends EstrategiaPrevisao {
    public PrevisaoMediaMovel(int tamanhoJanela) {
        super(tamanhoJanela);
    }

    @Override
    public double prever(List<Medicao> historico) {
        if (historico == null || historico.size() < tamanhoJanela()) {
            throw new DadosInvalidosException(
                    "São necessárias pelo menos " + tamanhoJanela() + " leituras para prever."
            );
        }

        double soma = 0.0;
        int inicio = historico.size() - tamanhoJanela();

        for (int indice = inicio; indice < historico.size(); indice++) {
            soma += historico.get(indice).valor();
        }

        return soma / tamanhoJanela();
    }
}