package com.htec.restapi;

import com.htec.model.City;
import com.htec.service.ICityService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/cities")
public class CityEndpoint {

    @Inject
    ICityService cityService;

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/get-cities")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCities() {

        List<City> cities = cityService.findAll();

        if (!cities.isEmpty()) {

            return Response.ok(cities).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/find-city/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCity(@PathParam("id") Long id) {

        City city = cityService.find(id);

        if (city.getIdCity() != null) {
            return Response.ok(city).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/find-city-by-name")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCityByName(@FormParam("name") String name) {

        List<City> city = cityService.findByName(name);

        if (city.size() > 0) {
            return Response.ok(city).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @RolesAllowed("ADMIN")
    @Path("/save-city")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response saveCity(@FormParam("name") String name,
                             @FormParam("country") String country,
                             @FormParam("description") String description) {

        City city = City.builder()
                .name(name)
                .country(country)
                .description(description).build();

        boolean r = cityService.save(city);

        if (r) {
            return Response.ok().status(Response.Status.CREATED).build();
        } else {
            return Response.notModified().build();
        }
    }

    @RolesAllowed("ADMIN")
    @Path("/update-city/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateCity(@FormParam("name") String name,
                               @FormParam("country") String country,
                               @FormParam("description") String description,
                               @PathParam("id") Long id) {

        City city = City.builder()
                .name(name)
                .country(country)
                .description(description).build();

        boolean r = cityService.update(city, id);

        if (r) {
            return Response.ok().status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.notModified().build();
        }
    }

    @RolesAllowed("ADMIN")
    @Path("/delete-city/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCity(@PathParam("id") Long id) {

        boolean r = cityService.delete(id);

        if (r) {
            return Response.ok().status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.notModified().build();
        }
    }
}
