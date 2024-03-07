package fr.concurrency.training.service;

import java.util.concurrent.CompletableFuture;

/**
 * @author gfourny
 */
public class CF {

    public static void main(String[] args) {

        getUser("id")
                .thenCompose(CF::getCreditForUser)
                .whenComplete(CF::printCredit)
                .join();
    }

    private static CompletableFuture<User> getUser(String userId) {
        return CompletableFuture.supplyAsync(() -> new User(userId));
    }

    private static CompletableFuture<Credit> getCreditForUser(User user) {
        return CompletableFuture.supplyAsync(() -> new Credit(user, 2.45));
    }

    private static void printCredit(Credit credit, Throwable throwable) {
        System.out.println(credit);
    }

    record User(String userId) {
    }

    record Credit(User user, Double credit) {
    }
}
