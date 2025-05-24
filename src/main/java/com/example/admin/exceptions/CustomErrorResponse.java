package com.example.admin.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
 public class CustomErrorResponse  {
    private String message;
    private LocalDateTime timestamp;
}