package com.mwaisaka.Library.Management.System.domain.mappers;

import com.mwaisaka.Library.Management.System.domain.Dto.BookDTO;
import com.mwaisaka.Library.Management.System.domain.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {
    BookDTO toDto(Book book);
    Book toEntity(BookDTO bookDTO);
}
