package com.example.serverpool.service;

import com.example.serverpool.model.Server;
import com.example.serverpool.repository.ServerRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import org.slf4j.Logger;

@Service
public class ResourceManagementService {
    final long MAX_CLOUD_SIZE = 100;

    @Autowired
    private ServerRepository serverRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManagementService.class);

    private Server findMatchedServer(Long size) throws Exception {
        Collection<Server> servers = serverRepository.findAllActiveServersWithMinSize(size);

        Server foundedServer = null;

        for(final Server server: servers) {
            if (foundedServer == null || server.getMemory() <= foundedServer.getMemory()) {
                foundedServer = server;
            }
        }

        if (foundedServer == null) {
            throw new Exception();
        }

        return foundedServer;
    }

    private void activateChildServers (Server server) {
        try {
            Collection<Server> childs = serverRepository.findByParentId(server.getId());

            for(final Server child: childs) {
                child.setActive(true);
                serverRepository.save(child);
            }
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }
    }


    private synchronized void spinNewServer() {
        try {
            Thread.sleep(20000);
        } catch (Exception ex) {}
    }


    public Server createServer(Long size) throws Exception {
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

        return instance;
    }
}
