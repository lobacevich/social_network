package by.senla.lobacevich.messenger.mapper;

public interface GenericMapper<Q, P, E> {

    P entityToDto(E entity);

    E dtoToEntity(Q dto);
}
