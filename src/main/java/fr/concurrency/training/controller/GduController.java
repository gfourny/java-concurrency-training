package fr.concurrency.training.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.concurrency.training.model.gdu.UtilisateurRefUtAppWithFonction;
import fr.concurrency.training.service.GduService;
import lombok.RequiredArgsConstructor;

/**
 * @author gfourny
 */
@RestController
@RequestMapping("/api/gdu")
@RequiredArgsConstructor
public class GduController {

    private final GduService gduService;

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<UtilisateurRefUtAppWithFonction>> getUsers() {
        return ResponseEntity.ok(gduService.retrieveUsersWithFonctions());
    }

    //TODO implémenter un autre endpoint non bloquant
}
