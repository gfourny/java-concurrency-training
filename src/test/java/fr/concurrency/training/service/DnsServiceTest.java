package fr.concurrency.training.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * @author gfourny
 */
class DnsServiceTest extends AbastractITSpring {

    @Autowired
    private DnsService dnsService;

    @Test
    @DisplayName("Doit retourner le DNS2")
    void should_return_the_DNS2() {
        var dns = dnsService.obtainFastestDns();

        assertAll(() -> {
            assertThat(dns).isNotNull();
            assertThat(dns.city()).isNotNull().isEqualTo("Paris");
        });
    }
}