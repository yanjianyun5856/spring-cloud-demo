package com.yjy.client.ping;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现 IPing 接口，检查 对象health是否正常状态码：200
 */
public class MyPing implements IPing {
    @Override
    public boolean isAlive(Server server) {
        String host = server.getHost();
        int port = server.getPort();
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.scheme("http");
        builder.host(host);
        builder.port(port);
        builder.path("/health");

        URI uri = builder.build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity responseEntity = restTemplate.getForEntity(uri,String.class);
        return HttpStatus.OK.equals(responseEntity.getStatusCode());
    }
}
