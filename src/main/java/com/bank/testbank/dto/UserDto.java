package com.bank.testbank.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @NotBlank
    @Size(min = 5)
    private String pin;
    @NotBlank
    @Size(min = 5)
    private String contactNo;
}
