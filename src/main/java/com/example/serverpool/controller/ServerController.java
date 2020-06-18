package com.example.serverpool.controller;

import com.example.serverpool.component.CreateServerThread;
import com.example.serverpool.config.AppConfig;
import com.example.serverpool.model.Server;
import com.example.serverpool.service.ResourceManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManagementService.class);

    @Autowired
    private ResourceManagementService resourceManagementService;

    private ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

    @PostMapping("/servers/allocate")
    public ResponseEntity<?> allocateServer(@RequestBody Server server) {
        try {
            CreateServerThread createServer = (CreateServerThread) ctx.getBean("createServerThread");

            createServer.setSize(server.getMemory());
            createServer.setResourceManagementService(resourceManagementService);
            createServer.start();

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
