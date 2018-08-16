package com.lordofthejars.diferencia.arquillian;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class V11Resource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getHello() {
        return "{"
            + "\"message\": \"Hello World\","
            + "\"name\": \"Alex\""
            + "}";
    }

}
