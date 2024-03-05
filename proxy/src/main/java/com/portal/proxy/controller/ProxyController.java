package com.portal.proxy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class ProxyController {

    @Value("${SERVICE_PORT}")
    public Integer port;

    private final RestTemplate restTemplate;

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/**")
    public ResponseEntity<String> proxy(RequestEntity<String> requestEntity) throws URISyntaxException {

        URI uri = new URI(
                "http", requestEntity.getUrl().getUserInfo(),
                "localhost", port, requestEntity.getUrl().getPath(),
                requestEntity.getUrl().getQuery(), requestEntity.getUrl().getFragment());

        RequestEntity<String> forwardEntity = new RequestEntity<>(
                requestEntity.getBody(), requestEntity.getHeaders(), requestEntity.getMethod(), uri);

        return restTemplate.exchange(forwardEntity, String.class);
    }
}
