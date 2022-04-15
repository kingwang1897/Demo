package com.alipay.sofa.endpoint.facade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/sofarest")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public interface SampleRestFacade {
    /**
     * http://localhost:8341/sofarest/hello
     */
    @GET
    @Path("/hello")
    String hello();
}
