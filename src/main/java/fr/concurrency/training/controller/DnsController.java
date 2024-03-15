package fr.concurrency.training.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.concurrency.training.model.dns.Dns;
import fr.concurrency.training.service.DnsService;
import lombok.RequiredArgsConstructor;

/**
 * @author gfourny
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DnsController {
    
    private final DnsService dnsService;
    
    @GetMapping("/dns")
    public Dns getFastestDns() {
        return dnsService.obtainFastestDns();
    }
}
