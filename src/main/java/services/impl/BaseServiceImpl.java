package services.impl;

import entities.BaseEntity;
import repos.BaseRepository;
import services.exception.UserServiceException;

import java.sql.SQLException;

public abstract class BaseServiceImpl<I, E extends BaseEntity<I>, R extends BaseRepository<E, I>> {

    protected abstract R getRepository();

    public E getById(I id) throws UserServiceException {
        try {
            return getRepository().findById(id).orElseThrow(() ->
                    new UserServiceException("entity not found with id: " + id));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserServiceException("error on getting user for id: " + id);
        }
    }

}
