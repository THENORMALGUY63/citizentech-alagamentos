import dominio.Local;
import dominio.RegistroAlagamento;
import dominio.StatusDrenagem;
import persistencia.RegistroAlagamentoRepositorio;
import servico.MonitoramentoAlagamentoServico;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        var repositorio = new RegistroAlagamentoRepositorio();
        var local = new Local("Praça Central", 12.0, StatusDrenagem.INSUFICIENTE);
        var horario = LocalDateTime.of(2026, 11, 12, 14, 0);

        repositorio.salvar(new RegistroAlagamento(local, horario, 0.0));
        repositorio.salvar(new RegistroAlagamento(local, horario.plusHours(1), 15.0));

        for (var registro : repositorio.listarTodos()) {
            System.out.println(registro.nivelAguaEmCentimetros() + " cm");
        }
    }
}