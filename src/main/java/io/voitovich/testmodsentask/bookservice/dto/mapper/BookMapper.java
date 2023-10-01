package io.voitovich.testmodsentask.bookservice.dto.mapper;


import io.voitovich.testmodsentask.bookservice.dto.BookDto;
import io.voitovich.testmodsentask.bookservice.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "id", source = "id")
    BookDto toDto(Book book);

    @Mapping(target = "id", source = "id")
    Book toEntity(BookDto bookDto);


}