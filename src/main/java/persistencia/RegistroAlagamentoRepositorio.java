package persistencia;

import dominio.RegistroAlagamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegistroAlagamentoRepositorio {
    private final List<RegistroAlagamento> registros = new ArrayList<>();

    public void salvar(RegistroAlagamento registro) {
        registros.add(Objects.requireNonNull(registro, "Informe o registro."));
    }

    public List<RegistroAlagamento> listarTodos() {
        return List.copyOf(registros);
    }
}