package persistencia;

import dominio.Alerta;

public interface GravadorAlerta {
    void salvar(Alerta alerta);
}