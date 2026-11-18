package persistencia;

import dominio.Alerta;
import dominio.NivelAlerta;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO implements GravadorAlerta {
    private final BancoDados banco;

    public AlertaDAO(BancoDados banco) {
        this.banco = banco;
    }

    @Override
    public void salvar(Alerta alerta) {
        String sql = """
                INSERT INTO alerta (
                    sensor_id, valor_previsto, limiar_atingido, nivel, data_hora_emissao
                ) VALUES (?, ?, ?, ?, ?)
                """;

        try (var conexao = banco.conectar();
             var comando = conexao.prepareStatement(sql)) {
            comando.setString(1, alerta.sensorId());
            comando.setDouble(2, alerta.valorPrevisto());
            comando.setDouble(3, alerta.limiarAtingido());
            comando.setString(4, alerta.nivel().name());
            comando.setObject(5, alerta.dataHoraEmissao());
            comando.executeUpdate();
        } catch (SQLException erro) {
            throw new IllegalStateException("Não foi possível gravar o alerta.", erro);
        }
    }

    public List<Alerta> listarTodos() throws SQLException {
        String sql = """
                SELECT sensor_id, valor_previsto, limiar_atingido, nivel, data_hora_emissao
                FROM alerta
                ORDER BY data_hora_emissao DESC, id DESC
                """;

        var alertas = new ArrayList<Alerta>();

        try (var conexao = banco.conectar();
             var comando = conexao.prepareStatement(sql);
             var resultado = comando.executeQuery()) {
            while (resultado.next()) {
                alertas.add(new Alerta(
                        resultado.getString("sensor_id"),
                        resultado.getDouble("valor_previsto"),
                        resultado.getDouble("limiar_atingido"),
                        NivelAlerta.valueOf(resultado.getString("nivel")),
                        resultado.getObject("data_hora_emissao", LocalDateTime.class)
                ));
            }
        }

        return List.copyOf(alertas);
    }
}