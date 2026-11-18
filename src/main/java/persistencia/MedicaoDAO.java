package persistencia;

import dominio.Medicao;
import excecao.DadosInvalidosException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedicaoDAO {
    private final BancoDados banco;

    public MedicaoDAO(BancoDados banco) {
        this.banco = banco;
    }

    public void salvar(Medicao medicao) throws SQLException {
        String sql = """
                INSERT INTO medicao (sensor_id, data_hora, valor)
                VALUES (?, ?, ?)
                """;

        try (var conexao = banco.conectar();
             var comando = conexao.prepareStatement(sql)) {
            comando.setString(1, medicao.sensorId());
            comando.setObject(2, medicao.dataHora());
            comando.setDouble(3, medicao.valor());
            comando.executeUpdate();
        }
    }

    public List<Medicao> buscarUltimasPorSensor(String sensorId, int quantidade)
            throws SQLException {
        if (sensorId == null || sensorId.isBlank() || quantidade < 1) {
            throw new DadosInvalidosException(
                    "Informe o ID do sensor e uma quantidade positiva de leituras."
            );
        }

        String sql = """
                SELECT sensor_id, data_hora, valor
                FROM medicao
                WHERE sensor_id = ?
                ORDER BY data_hora DESC, id DESC
                LIMIT ?
                """;

        var medicoes = new ArrayList<Medicao>();

        try (var conexao = banco.conectar();
             var comando = conexao.prepareStatement(sql)) {
            comando.setString(1, sensorId);
            comando.setInt(2, quantidade);

            try (var resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    medicoes.add(new Medicao(
                            resultado.getString("sensor_id"),
                            resultado.getObject("data_hora", LocalDateTime.class),
                            resultado.getDouble("valor")
                    ));
                }
            }
        }

        Collections.reverse(medicoes);
        return List.copyOf(medicoes);
    }
}