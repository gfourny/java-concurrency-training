package fr.concurrency.training.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.concurrency.training.model.gdu.UtilisateurRefUtAppWithFonction;
import fr.concurrency.training.service.GduService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gfourny
 */
@RestController
@RequestMapping("/api/gdu")
@RequiredArgsConstructor
@Slf4j
public class GduController {

    private final GduService gduService;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    //Implémentation classique, model 1 thread per request
    //spring.threads.virtual.enabled=true permet de créer 1 thread virtuel par requête
    @GetMapping("/utilisateurs")
    public List<UtilisateurRefUtAppWithFonction> getUsers() {
        return gduService.retrieveUsersWithFonctions();
    }

    // ! N'équivaux pas à utiliser spring.threads.virtual.enabled=true car le thread http est bloqué dû au get()
    @GetMapping("/utilisateurs/virtual")
    public List<UtilisateurRefUtAppWithFonction> getUsersVirtual() {
        try {
            return executor.submit(gduService::retrieveUsersWithFonctions).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    // Pas utile dans notre cas, car renvoi l'état de la Future et non la liste des Utilisateurs
    @GetMapping("/utilisateurs/virtual-non-blocking")
    public Future<List<UtilisateurRefUtAppWithFonction>> getUsersVirtualNonBlocking() {
        return executor.submit(gduService::retrieveUsersWithFonctions);
    }

    //On libère tout de suite le thread http (nio -> conf tomcat)
    //Si on ne spécifie par d'executorService --> CommonForkJoinPool
    @GetMapping("/utilisateurs/non-bloquant")
    public CompletableFuture<List<UtilisateurRefUtAppWithFonction>> getUsersNonBloquant() {
        return CompletableFuture.supplyAsync(gduService::retrieveUsersWithFonctions);
    }

    //On délègue à Spring la gestion asynchrone
    @Async
    @GetMapping("utilisateurs/async")
    public CompletableFuture<List<UtilisateurRefUtAppWithFonction>> getUsersAsync() {
        return gduService.retrieveUsersWithFonctionsOtherSolution();
    }
}
