package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;

import java.util.List;

public interface GenericService<Q, P, E> {

    P updateEntity(Q requestDto, Long id) throws EntityNotFoundException, InvalidDataException;

    P findById(Long id) throws EntityNotFoundException;

    List<P> findAll(int pageSize, int pageNumber);

    void deleteEntity(Long id);

    E findEntityById(Long id) throws EntityNotFoundException;
}
