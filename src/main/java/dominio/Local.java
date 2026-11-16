package dominio;

public record Local(
        String nome,
        double elevacaoEmMetros,
        StatusDrenagem statusDrenagem
) {
    public Local {
    if (nome == null || nome.isBlank()) {
        throw new IllegalArgumentException("Informe o nome do local.");
    }

    if (!Double.isFinite(elevacaoEmMetros)) {
        throw new IllegalArgumentException("A elevação deve ser um número finito.");
    }

    if (statusDrenagem == null) {
        throw new IllegalArgumentException(
                "Informe a condição da drenagem ou use DESCONHECIDA."
        );
    }
}
}