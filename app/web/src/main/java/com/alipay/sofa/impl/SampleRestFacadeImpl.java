package com.alipay.sofa.impl;


import com.alipay.sofa.endpoint.facade.SampleRestFacade;

import com.alipay.sofa.runtime.api.annotation.SofaService;
import com.alipay.sofa.runtime.api.annotation.SofaServiceBinding;
import org.springframework.stereotype.Service;

/**
 * REST service interface implementation.
 */
@Service
@SofaService(interfaceType = SampleRestFacade.class,bindings = @SofaServiceBinding(bindingType = "rest"))
public class SampleRestFacadeImpl implements SampleRestFacade {
    private int count = 0;
    public SampleRestFacadeImpl(){
        System.out.println("print start");
    }
    @Override
    public String hello() {
        return "Hello SOFARest! times = " + count++;
    }
}
