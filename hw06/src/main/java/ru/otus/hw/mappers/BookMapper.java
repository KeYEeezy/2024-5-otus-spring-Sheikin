package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

     BookDto toDto(Book source);

     Book toEntity(BookDto source);
}
