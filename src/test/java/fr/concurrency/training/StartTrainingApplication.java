package fr.concurrency.training;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;

import com.github.tomakehurst.wiremock.WireMockServer;

import fr.concurrency.training.config.ContainersConfig;
import lombok.val;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * @author gfourny
 */
public class StartTrainingApplication {

    public static void main(String[] args) {
        WireMockServer wm = new WireMockServer(options()
                .dynamicPort()
                .asynchronousResponseEnabled(true)
                .asynchronousResponseThreads(200)
        );
        wm.start();

        val serverPort = String.format("--wiremock.server.port=%s", wm.port());

        val isPortConfigured = Arrays.stream(args).noneMatch(s -> s.contains("wiremock.server.port"));

        if (isPortConfigured) {
            args = appendTo(args, serverPort);
        }

        args = appendTo(args, "--spring.profiles.active=dev");

        SpringApplication
                .from(TrainingApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }

    private static String[] appendTo(String[] arr, String str) {
        String[] result = new String[arr.length + 1];

        System.arraycopy(arr, 0, result, 0, arr.length);
        result[result.length - 1] = str;

        return result;
    }
}