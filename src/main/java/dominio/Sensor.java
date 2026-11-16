package dominio;

public interface Sensor {
    String id();

    String tipo();

    boolean verificarFuncionamento();

    Medicao ler();
}