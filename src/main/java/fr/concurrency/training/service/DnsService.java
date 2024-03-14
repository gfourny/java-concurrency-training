package fr.concurrency.training.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;

import org.springframework.stereotype.Service;

import fr.concurrency.training.model.dns.Dns;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author gfourny
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DnsService {

    private static final String DNS_1 = "dns1";
    private static final String DNS_2 = "dns2";
    private static final String DNS_3 = "dns3";

    private final Apis apis;

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Solution permettant de traiter la réponse la plus rapide uniquement.<br/>
     * Lorsqu'une {@link CompletableFuture} est terminée, les autres sont <b>annulées</b>.<br/>
     * Plus efficient qu'une {@link CompletableFuture#allOf(CompletableFuture[])} qui elle <b>attendra le retour de tous les appels</b>
     *
     * @return {@link Dns}
     */
    public Dns obtainFastestDns() {
        return CompletableFuture.anyOf(
                        CompletableFuture.supplyAsync(() -> apis.fetchDns(DNS_1), executorService),
                        CompletableFuture.supplyAsync(() -> apis.fetchDns(DNS_2), executorService),
                        CompletableFuture.supplyAsync(() -> apis.fetchDns(DNS_3), executorService)
                )
                .thenApply(Dns.class::cast)
                .join();
    }

    /**
     * Solution <b>java 23+</b><br/>
     * Écriture plus impérative et plus claire en utilisant {@link StructuredTaskScope.ShutdownOnSuccess}.<br/>
     * On précise le type {@link Dns} de la {@link StructuredTaskScope} afin de le récupérer sur le scope.<br/>
     * Comme son nom l'indique, l'implementation {@link StructuredTaskScope.ShutdownOnSuccess} permet de ne pas attendre les autres réponses.
     *
     * @return {@link Dns}
     */
    public Dns obtainFastestDnsWithSC() {
        try (val scope = new StructuredTaskScope.ShutdownOnSuccess<Dns>()) {

            scope.fork(() -> apis.fetchDns(DNS_1));
            scope.fork(() -> apis.fetchDns(DNS_2));
            scope.fork(() -> apis.fetchDns(DNS_3));

            scope.join();

            return scope.result();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
