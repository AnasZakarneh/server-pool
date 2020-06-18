package com.example.serverpool.service;

import com.example.serverpool.model.Server;
import com.example.serverpool.repository.ServerRepository;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ResourceManagementService {
    final long MAX_CLOUD_SIZE = 100;

    @Autowired
    private ServerRepository serverRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManagementService.class);

    private Server findMatchedServer(Long size) throws Exception {
        List<Server> servers = serverRepository.findAll();

        Server foundedServer = null;

        for(final Server server: servers) {
            if (server.getMemory() >= size && (foundedServer == null || server.getMemory() <= foundedServer.getMemory()) && server.isCloud()) {
                foundedServer = server;
            }
        }

        if (foundedServer == null) {
            throw new Exception();
        }

        return foundedServer;
    }


    private void spinNewServer() {
        try {
            Thread.sleep(20000);
        } catch (Exception ex) {}
    }

    private void activateChildServers (Server server) {
        try {
            serverRepository.findById(server.getId()).map(parent -> {
                Set<Server>  servers = parent.getChilds();
                for(final Server child: servers) {
                    child.setActive(true);
                    serverRepository.save(child);
                }

                return parent;
            });
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }
    }

    @Async
    public CompletableFuture<Server> createServer(Long size) throws Exception {
        Server server;
        try {
            server = findMatchedServer(size);
            server.setMemory(server.getMemory() - size);
        } catch (Exception ex) {
            // create cloud server
            server = serverRepository.save(new Server(MAX_CLOUD_SIZE - size, true, false));
            spinNewServer();
            server.setActive(true);
            activateChildServers(server);
        }

        // update cloud server
        serverRepository.save(server);

        // create new server
        Server instance = new Server(size, false, server.isActive());


        try {
            instance = serverRepository.save(instance);
            instance.setParent(server);
            instance = serverRepository.save(instance);
        }catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }

        return CompletableFuture.completedFuture(instance);
    }
}
