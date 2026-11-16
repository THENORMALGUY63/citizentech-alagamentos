package servico;

import dominio.RegistroAlagamento;
import persistencia.RegistroAlagamentoRepositorio;

import java.util.List;

public class MonitoramentoAlagamentoServico {
    private final RegistroAlagamentoRepositorio repositorio;

    public MonitoramentoAlagamentoServico(RegistroAlagamentoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<RegistroAlagamento> listarObservacoesComAgua() {
        return repositorio.listarTodos().stream()
                .filter(registro -> registro.nivelAguaEmCentimetros() > 0)
                .toList();
    }
    public void registrarObservacao(RegistroAlagamento registro) {
    repositorio.salvar(registro);
    }   
}