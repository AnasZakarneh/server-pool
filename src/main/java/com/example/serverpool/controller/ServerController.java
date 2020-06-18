package com.example.serverpool.controller;

import com.example.serverpool.model.Server;
import com.example.serverpool.service.ResourceManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/servers/allocate")
    public ResponseEntity<?> allocateServer(@RequestBody Server server) {
        try {
            resourceManagementService.createServer(server.getMemory());

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
