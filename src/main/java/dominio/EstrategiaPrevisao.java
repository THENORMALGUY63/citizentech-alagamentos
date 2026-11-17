package dominio;

import excecao.DadosInvalidosException;

import java.util.List;

public abstract class EstrategiaPrevisao {
    private final int tamanhoJanela;

    protected EstrategiaPrevisao(int tamanhoJanela) {
        if (tamanhoJanela < 1) {
            throw new DadosInvalidosException("O tamanho da janela deve ser positivo.");
        }

        this.tamanhoJanela = tamanhoJanela;
    }

    protected int tamanhoJanela() {
        return tamanhoJanela;
    }

    // Recebe medições de um único sensor, da mais antiga para a mais recente.
    public abstract double prever(List<Medicao> historico);
}