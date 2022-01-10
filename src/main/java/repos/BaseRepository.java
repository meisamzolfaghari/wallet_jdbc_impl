package repos;

import config.ConnectionFactory;
import entities.BaseEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// TODO: 1/10/2022 try to generify repositories
public abstract class BaseRepository<E extends BaseEntity<I>, I> {

    protected final Connection connection = ConnectionFactory.getConnection();

    public abstract void create(E entity) throws SQLException;

    public abstract Optional<E> findById(I id) throws SQLException;

    public abstract void update(E entity) throws SQLException;

    public abstract List<E> findAll() throws SQLException;

    protected abstract String getTableName();

    @SuppressWarnings("unchecked")
    protected void setCreatedId(E entity, PreparedStatement statement) throws SQLException {
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next())
            entity.setId((I) generatedKeys.getObject(1));
        else throw new SQLException("Creating entity failed, no ID obtained!");
    }

    public Connection getConnection() {
        return connection;
    }

}
