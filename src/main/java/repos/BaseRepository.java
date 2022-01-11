package repos;

import entities.BaseEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// TODO: 1/10/2022 try to generify repositories
public abstract class BaseRepository<E extends BaseEntity<I>, I> {

    public abstract void create(E entity, Connection connection) throws SQLException;

    public abstract Optional<E> findById(I id, Connection connection) throws SQLException;

    public abstract void update(E entity, Connection connection) throws SQLException;

    public abstract List<E> findAll(Connection connection) throws SQLException;

    protected abstract String getTableName();

}
