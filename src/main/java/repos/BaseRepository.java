package repos;

import config.ConnectionFactory;
import entities.BaseEntity;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T extends BaseEntity<I>, I> {

    protected final Connection connection = ConnectionFactory.getConnection();

    public abstract void create(T entity) throws SQLException;

    public abstract Optional<T> findById(I id) throws SQLException;

    public abstract void update(T entity) throws SQLException;

    public abstract List<T> findAll() throws SQLException;

    public T getById(I id) throws SQLException {
        return findById(id).orElseThrow(() ->
                new IllegalStateException("entity not found with id: " + id));
    }

    protected abstract String getTableName();

}
