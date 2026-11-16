package dominio;

import excecao.DadosInvalidosException;

public record LimiaresAlerta(double atencao, double alerta, double critico) {
    public LimiaresAlerta {
        if (!Double.isFinite(atencao)
                || !Double.isFinite(alerta)
                || !Double.isFinite(critico)
                || atencao < 0
                || atencao >= alerta
                || alerta >= critico) {
            throw new DadosInvalidosException(
                    "Os limiares devem ser finitos e seguir a ordem: "
                            + "0 <= atenção < alerta < crítico."
            );
        }
    }
}