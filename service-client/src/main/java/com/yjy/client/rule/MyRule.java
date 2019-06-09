package com.yjy.client.rule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

import java.util.List;
import java.util.Random;

/**
 * 自定义实现IRule规则
 * 永远选择最后一台可达的服务
 */
public class MyRule extends AbstractLoadBalancerRule {
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        ILoadBalancer loadBalancer = getLoadBalancer();
        //获取所有可达的服务列表
        List<Server> servers = loadBalancer.getAllServers();

        int i =  new Random().nextInt(2);
        if(servers == null){
            return null;
        }
        System.out.println("---------------------------------------------"+i);
        Server targetServer = servers.get(i);

        return targetServer;
    }
}
