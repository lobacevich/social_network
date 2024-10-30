package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.entity.AppEntity;
import by.senla.lobacevich.messenger.exception.AuthorizationException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.mapper.GenericMapper;
import by.senla.lobacevich.messenger.service.GenericService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AbstractService<Q, P, E extends AppEntity, R extends JpaRepository<E, Long>,
        M extends GenericMapper<Q, P, E>> implements GenericService<Q, P, E> {

    protected final R repository;
    protected final M mapper;

    public AbstractService(R repository, M mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public P createEntity(Q request) throws InvalidDataException, EntityNotFoundException, AuthorizationException {
        return mapper.entityToDto(repository.save(mapper.dtoToEntity(request)));
    }

    @Override
    public P updateEntity(Q request, Long id) throws EntityNotFoundException, InvalidDataException {
        E entity = mapper.dtoToEntity(request);
        entity.setId(id);
        return mapper.entityToDto(repository.save(entity));
    }

    @Override
    public P findById(Long id) throws EntityNotFoundException {
        return mapper.entityToDto(findEntityById(id));
    }

    @Override
    public List<P> findAll(int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return repository.findAll(pageable)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEntity(Long id) {
        repository.deleteById(id);
    }

    @Override
    public E findEntityById(Long id) throws EntityNotFoundException {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Entity with id " + id + " not found"));
    }
}
