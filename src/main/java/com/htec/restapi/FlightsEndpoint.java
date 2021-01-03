package com.htec.restapi;

import com.htec.model.City;
import com.htec.model.FlightResult;
import com.htec.service.FindFlightsService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/flights")
public class FlightsEndpoint {

    @Inject
    FindFlightsService findFlightsService;

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/find-by-source-and-destination")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findBySourceAndDestination(@FormParam("source") String source,
                                   @FormParam("destination") String destination) {

        List<FlightResult> flightResults = findFlightsService.findBySourceAndDestination(source,destination);
        if (flightResults.size() > 0) {
            return Response.ok(flightResults).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
