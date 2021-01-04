package com.htec.restapi;

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

    /**
     * Provides search for flights based on airport codes for source and destination airport. Depth or better say number
     * of allowed transits between source airport until arrival to destination airport is controlled by <code>NUMBER_OF_TRANSITES</code>
     * parameter inside properties file.
     * @param source source airport
     * @param destination destination airport
     * @return JSON representation of all available flights in ascending order based on flight price.
     */
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
