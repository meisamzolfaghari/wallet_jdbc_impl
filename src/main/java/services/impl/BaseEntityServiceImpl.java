package services.impl;

import entities.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repos.BaseRepository;

import java.sql.SQLException;
import java.util.NoSuchElementException;

public abstract class BaseEntityServiceImpl<I, E extends BaseEntity<I>, R extends BaseRepository<E, I>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEntityServiceImpl.class);

    protected abstract R getRepository();

    public E getById(I id) {
        try {
            return getRepository().findById(id).orElseThrow(() ->
                    new NoSuchElementException("entity not found with id: " + id));
        } catch (SQLException e) {
            final String message = String.format("error on getting entity for id: %s ", id);
            LOGGER.error(message, e);
            throw new NoSuchElementException(message);
        }
    }

}
