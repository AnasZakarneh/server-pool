package com.example.serverpool.component;

import com.example.serverpool.service.ResourceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CreateServerThread extends Thread {
    private long size;
    private ResourceManagementService resourceManagementService;

    @Override
    public void run() {
        try {
            getResourceManagementService().createServer(getSize());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setResourceManagementService(ResourceManagementService resourceManagementService) {
        this.resourceManagementService = resourceManagementService;
    }

    public ResourceManagementService getResourceManagementService() {
        return resourceManagementService;
    }
}
