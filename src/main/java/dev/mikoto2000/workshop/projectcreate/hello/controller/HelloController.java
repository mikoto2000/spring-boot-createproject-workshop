package dev.mikoto2000.workshop.projectcreate.hello.controller;

import dev.mikoto2000.workshop.projectcreate.hello.dto.HelloResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {
    @GetMapping
    public HelloResponse sayHello() {
        return new HelloResponse("Hello, World!");
    }
}

