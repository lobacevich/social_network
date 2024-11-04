package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;

import java.util.List;

public interface GenericService<Q, P, E> {

    P createEntity(Q requestDto) throws EntityNotFoundException, AccessDeniedException, InvalidDataException;

    P updateEntity(Q requestDto, Long id) throws EntityNotFoundException, InvalidDataException, AccessDeniedException;

    P findById(Long id) throws EntityNotFoundException;

    List<P> findAll(int pageSize, int pageNumber);

    void deleteEntity(Long id) throws EntityNotFoundException;

    E findEntityById(Long id) throws EntityNotFoundException;
}
