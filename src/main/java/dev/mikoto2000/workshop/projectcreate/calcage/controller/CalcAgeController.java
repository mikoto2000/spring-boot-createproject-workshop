package dev.mikoto2000.workshop.projectcreate.calcage.controller;

import dev.mikoto2000.workshop.projectcreate.calcage.dto.CalcAgeResponse;
import dev.mikoto2000.workshop.projectcreate.calcage.service.CalcAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/calc-age")
public class CalcAgeController {

     private final CalcAgeService calcAgeService;

     @Autowired
     public CalcAgeController(CalcAgeService calcAgeService) {
          this.calcAgeService = calcAgeService;
     }

     @GetMapping
     public CalcAgeResponse calculateAge(@RequestParam("birthDay") String birthDay) {
          try {
            LocalDate birthDate = LocalDate.parse(birthDay);
            int age = calcAgeService.calculateAge(birthDate);
            return new CalcAgeResponse(age);
          } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use ISO 8601 format (YYYY-MM-DD).");
          }
     }
}

