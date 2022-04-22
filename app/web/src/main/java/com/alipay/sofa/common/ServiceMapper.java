package com.alipay.sofa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServiceMapper<T, S extends Typeable<T>> implements InitializingBean, ServiceProvider<T, S> {

    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Resource
    private List<S> services;

    private Map<T, S> serviceMqp;

    @Override
    public void afterPropertiesSet() throws Exception {
        // Check
        Asserts.notEmpty(services, "No service found for target interface");
        // Mapping Service
        this.serviceMqp = this.services
                .stream()
                .collect(Collectors.toMap(S::getType, Function.identity()));
    }

    @Override
    public S getService(T type) {
        return serviceMqp.get(type);
    }
}
