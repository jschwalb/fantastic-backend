package de.universa.eval.fantasticbackend.calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping(path = "calc")
@Slf4j
public class CalculatorController {

    @GetMapping("/{first}+{second}")
    public long add(@PathVariable("first") long first, @PathVariable("second") long second) {
        return first + second;
    }

    @GetMapping("/{first}x{second}")
    public long multiply(@PathVariable("first") long first, @PathVariable("second") long second) {
        return first * second;
    }

    @GetMapping("/{first}-{second}")
    public long minus(@PathVariable("first") long first, @PathVariable("second") long second) {
        return first - second;
    }

    @GetMapping("/{first}div{second}")
    public BigDecimal divide(@PathVariable("first") long first, @PathVariable("second") long second) {
        return BigDecimal.valueOf(first).divide(BigDecimal.valueOf(second), 2, RoundingMode.HALF_UP);
    }

    @GetMapping("/{first}mod{second}")
    public long modulo(@PathVariable("first") long first, @PathVariable("second") long second) {
        return first % second;
    }

}
