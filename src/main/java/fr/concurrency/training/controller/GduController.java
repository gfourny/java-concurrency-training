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

    /**
     * Implémentation classique, model 1 thread per request <br/>
     * <b>spring.threads.virtual.enabled=true</b> permet de créer 1 thread virtuel par requête
     *
     * @return {@link List} d'{@link UtilisateurRefUtAppWithFonction}
     */
    @GetMapping("/utilisateurs")
    public List<UtilisateurRefUtAppWithFonction> getUsers() {
        return gduService.retrieveUsersWithFonctions();
    }

    /**
     * <b>! N'équivaux pas à utiliser spring.threads.virtual.enabled=true car le thread http est bloqué dû au get()</b>
     *
     * @return {@link List} d'{@link UtilisateurRefUtAppWithFonction}
     */
    @GetMapping("/utilisateurs/virtual")
    public List<UtilisateurRefUtAppWithFonction> getUsersVirtual() {
        try {
            return executor.submit(gduService::retrieveUsersWithFonctions).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Pas utile dans notre cas, car renvoi l'état de la Future et non la liste des Utilisateurs
     *
     * @return {@link Future}
     */
    @GetMapping("/utilisateurs/virtual-non-blocking")
    public Future<List<UtilisateurRefUtAppWithFonction>> getUsersVirtualNonBlocking() {
        return executor.submit(gduService::retrieveUsersWithFonctions);
    }

    /**
     * On libère tout de suite le thread http (nio -> conf tomcat) <br/>
     * Si on ne spécifie par d'executorService --> CommonForkJoinPool
     *
     * @return {@link CompletableFuture}
     */
    @GetMapping("/utilisateurs/non-bloquant")
    public CompletableFuture<List<UtilisateurRefUtAppWithFonction>> getUsersNonBloquant() {
        return CompletableFuture.supplyAsync(gduService::retrieveUsersWithFonctions);
    }

    /**
     * C'est tentant : Mais n'a aucune utilité ici <br/>
     * Une méthode publique annotée d'Async est proxifiée par Spring pour spécifier que l'appelant n'a pas besoin d'attendre la réponse
     * Ce qui n'est absolument pas notre cas ici.<br/>
     * Je ne suis pas super fan de cette annotation, car on abstrait trop ce qui est fait derrière.<br/>
     * Ni plus ni moins qu'un executorService.submit(()-> votreMéhtodePublic()), un peu overkill un pattern proxy pour ça.<br/>
     * Cela peut être utile dans certains cas (écriture d'un log, d'un fichier) dont le succès ou l'échec importe peu
     *
     * @return {@link CompletableFuture}
     * @see <a href="https://www.baeldung.com/spring-async">tuto baeldung</a>
     */
    @Async
    @GetMapping("utilisateurs/async")
    public CompletableFuture<List<UtilisateurRefUtAppWithFonction>> getUsersAsync() {
        return gduService.retrieveUsersWithFonctionsOtherSolution();
    }
}
