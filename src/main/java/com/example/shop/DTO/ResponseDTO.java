package com.example.shop.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDTO<T> {
    private T data;
    private long requestDurationInMs;
}
