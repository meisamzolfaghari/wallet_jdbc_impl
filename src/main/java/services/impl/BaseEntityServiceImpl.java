package services.impl;

import entities.BaseEntity;
import repos.BaseRepository;

public abstract class BaseEntityServiceImpl<I, E extends BaseEntity<I>, R extends BaseRepository<E, I>> {

    protected abstract R getRepository();

}
