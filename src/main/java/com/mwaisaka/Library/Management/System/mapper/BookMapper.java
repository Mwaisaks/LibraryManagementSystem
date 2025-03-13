package com.mwaisaka.Library.Management.System.mapper;


import com.mwaisaka.Library.Management.System.Dto.BookDTO;
import com.mwaisaka.Library.Management.System.models.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO toDto(Book book);

    Book toEntity(BookDTO bookDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookFromDto(BookDTO bookDTO, @MappingTarget Book book);
}

