package dominio;

import java.util.List;

public class PontoMonitorado extends ComponenteTerritorial {
    private final Local local;
    private final List<Sensor> sensores;

    public PontoMonitorado(
            String id, String regiao, boolean ativo, Local local, List<Sensor> sensores
    ) {
        super(id, local.nome(), regiao, ativo);
        this.local = local;
        this.sensores = List.copyOf(sensores);
    }

    public Local local() {
        return local;
    }

    @Override
    public boolean verificarStatus() {
        return estaAtivo()
                && !sensores.isEmpty()
                && sensores.stream().allMatch(Sensor::verificarFuncionamento);
    }
}