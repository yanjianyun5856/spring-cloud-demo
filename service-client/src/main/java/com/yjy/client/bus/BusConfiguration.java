package com.yjy.client.bus;

import org.springframework.cloud.bus.event.EnvironmentChangeRemoteApplicationEvent;
import org.springframework.cloud.bus.event.RefreshRemoteApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 自定义  RefreshRemoteApplicationEvent 监听
 */
@Configuration
public class BusConfiguration {

    @EventListener
    public void onRefreshRemoteApplicationEvent(RefreshRemoteApplicationEvent event){

        System.out.printf("RefreshRemoteApplicationEvent sours: %s, originService %s, destinationService %s \n",
                event.getSource(),
                event.getOriginService(),
                event.getDestinationService());

    }

    @EventListener
    public void onEnvironmentChangeRemoteApplicationEvent(EnvironmentChangeRemoteApplicationEvent event){
        System.out.printf("EnvironmentChangeRemoteApplicationEvent sours: %s, originService %s, destinationService %s \n",
                event.getSource(),
                event.getOriginService(),
                event.getDestinationService());
    }

}
