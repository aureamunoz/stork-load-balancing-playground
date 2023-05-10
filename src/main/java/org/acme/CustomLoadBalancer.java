package org.acme;

import java.util.Collection;
import java.util.Random;

import io.smallrye.stork.api.LoadBalancer;
import io.smallrye.stork.api.NoServiceInstanceFoundException;
import io.smallrye.stork.api.ServiceInstance;

public class CustomLoadBalancer implements LoadBalancer {


    public CustomLoadBalancer(CustomLoadBalancerConfiguration config) {
    }

    @Override
    public ServiceInstance selectServiceInstance(Collection<ServiceInstance> serviceInstances) {
        if (serviceInstances.isEmpty()) {
            throw new NoServiceInstanceFoundException("No services found.");
        }
        for (ServiceInstance serviceInstance :serviceInstances) {
            if (serviceInstance.getLabels().containsKey("Woodstock")){
                return serviceInstance;
            }

        }
        return null;
    }

    @Override
    public boolean requiresStrictRecording() {
        return false;
    }
}

