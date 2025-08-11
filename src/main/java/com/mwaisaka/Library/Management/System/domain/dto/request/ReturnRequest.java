package com.mwaisaka.Library.Management.System.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReturnRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID bookId;


}
