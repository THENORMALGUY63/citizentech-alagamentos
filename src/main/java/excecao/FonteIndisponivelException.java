package excecao;

public class FonteIndisponivelException extends IllegalStateException {
    public FonteIndisponivelException(String mensagem) {
        super(mensagem);
    }
}