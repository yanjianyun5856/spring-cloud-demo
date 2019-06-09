package com.yjy.client.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;

public class RibbonClientHystrixCommand extends HystrixCommand<Collection> {

    private String serviceProviderName;

    private RestTemplate restTemplate;

    public RibbonClientHystrixCommand(String serviceProviderName, RestTemplate restTemplate) {
        super(HystrixCommandGroupKey.Factory.asKey("ribbon-client"),
                100);
        this.serviceProviderName = serviceProviderName;
        this.restTemplate = restTemplate;
    }

    /**
     * 主逻辑实现
     * @return
     * @throws Exception
     */
    @Override
    protected Collection run() throws Exception {
        return restTemplate.getForObject("http://"+serviceProviderName+"/user/list",Collection.class);
    }

    /**
     * fallback 覆盖实现
     * @return
     */
    @Override
    protected Collection getFallback() {
        System.out.println("RibbonClient fallback 方法");
        return Collections.emptyList();
    }
}
