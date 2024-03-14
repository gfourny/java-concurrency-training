package fr.concurrency.training.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.concurrency.training.model.Dilly;
import fr.concurrency.training.service.DillyService;
import lombok.RequiredArgsConstructor;

/**
 * @author gfourny
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DillyController {
    
    private final DillyService dillyService;
    
    @GetMapping("/dilly")
    public Dilly commandDilly(){
        return dillyService.command();
    }
}
