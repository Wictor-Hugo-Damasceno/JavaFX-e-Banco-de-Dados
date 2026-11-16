
package model;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

public class PlayerRepositorio {

    private Database database;
    private Dao<Player, Integer> dao;

    public PlayerRepositorio(Database database) {
        this.database = database;

        try {
            dao = DaoManager.createDao(
                database.getConnection(),
                Player.class
            );

            TableUtils.createTableIfNotExists(
                database.getConnection(),
                Player.class
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                "Erro ao inicializar o repositório de Player.", e
            );
        }
    }

    public Player create(Player player) {
        try {
            dao.create(player);
            return player;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar Player.", e);
        }
    }

    public void update(Player player) {
        try {
            dao.update(player);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Player.", e);
        }
    }

    public void delete(Player player) {
        try {
            dao.delete(player);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir Player.", e);
        }
    }

    public Player loadFromId(int id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Player.", e);
        }
    }

    public List<Player> loadAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Players.", e);
        }
    }
}
