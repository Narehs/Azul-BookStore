package org.bookstore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BaseResponse {

    private Long id;

    @JsonProperty("created_at")
    private LocalDateTime createdDateTime;

    @JsonProperty("updated_at")
    private LocalDateTime updatedDateTime;

    private String createdBy;

    private String updatedBy;
}
