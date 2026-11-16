package dominio;

public abstract class ComponenteTerritorial {
    private final String id;
    private final String nome;
    private final String regiao;
    private boolean ativo;

    protected ComponenteTerritorial(String id, String nome, String regiao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.ativo = ativo;
    }

    public String id() {
        return id;
    }

    public String nome() {
        return nome;
    }

    public String regiao() {
        return regiao;
    }

    public boolean estaAtivo() {
        return ativo;
    }

    public abstract boolean verificarStatus();
}