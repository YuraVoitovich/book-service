package io.voitovich.testmodsentask.bookservice.dto.mapper;


import io.voitovich.testmodsentask.bookservice.dto.BookDto;
import io.voitovich.testmodsentask.bookservice.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
    BookDto toDto(Book book);

    Book toEntity(BookDto bookDto);


}
