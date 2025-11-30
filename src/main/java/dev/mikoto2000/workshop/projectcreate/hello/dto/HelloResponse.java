package dev.mikoto2000.workshop.projectcreate.hello.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class HelloResponse {
    private String message;
}
