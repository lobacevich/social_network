package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

import java.util.List;

public interface GenericService<Q, P, E> {

    P createEntity(Q q) throws InvalidDataException, EntityNotFoundException;

    P updateEntity(Q requestDto, Long id) throws EntityNotFoundException, InvalidDataException;

    P findById(Long id) throws EntityNotFoundException;

    List<P> findAll(int pageSize, int pageNumber);

    void deleteEntity(Long id);

    E findEntityById(Long id) throws EntityNotFoundException;
}
