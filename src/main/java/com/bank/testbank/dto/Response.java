package com.bank.testbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private String status;
    private String message;

    public Response(String message) {
        this.message = message;
    }
}
