package com.dmika.sarafan.dto;

import com.dmika.sarafan.domain.Views;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonView(Views.Id.class)
public class WsEventDto {

    private ObjectType objectType;
    private EventType eventType;
    @JsonRawValue
    private String body;

}
