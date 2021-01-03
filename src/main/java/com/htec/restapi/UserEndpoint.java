package com.htec.restapi;

import com.htec.model.User;
import com.htec.service.IUserService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
public class UserEndpoint {

    @Inject
    private IUserService userService;

    @RolesAllowed("ADMIN")
    @Path("/get-users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {

        List<User> users = userService.findAll();

        if (!users.isEmpty()) {

            return Response.ok(users).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/find-user/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUser(@PathParam("id") Long id) {

        User user = userService.find(id);

        if (user.getIdUser() != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/save-user")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response saveUser(@FormParam("firstname") String firstName,
                             @FormParam("lastname") String lastName,
                             @FormParam("username") String username,
                             @FormParam("password") String password) {

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(password).build();

        boolean r = userService.save(user);

        if (r) {
            return Response.ok().status(Response.Status.CREATED).build();
        } else {
            return Response.notModified().build();
        }
    }

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/update-user/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateUser(@FormParam("firstname") String firstName,
                               @FormParam("lastname") String lastName,
                               @FormParam("username") String username,
                               @FormParam("password") String password,
                               @PathParam("id") Long id) {

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(password).build();

        boolean r = userService.update(user, id);

        if (r) {
            return Response.ok().status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.notModified().build();
        }
    }

    @RolesAllowed("ADMIN")
    @Path("/delete-user/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Long id) {

        boolean r = userService.delete(id);

        if (r) {
            return Response.ok().status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.notModified().build();
        }
    }

    @RolesAllowed("ADMIN")
    @Path("/set-role/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRole(@FormParam("role") String role,
                               @PathParam("id") Long id) {

        boolean r = userService.updateRole(role, id);

        if (r) {
            return Response.ok().status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.notModified().build();
        }
    }

    @RolesAllowed("ADMIN")
    @Path("/register/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@PathParam("id") Long id) {

        boolean r = userService.register(id);

        if (r) {
            return Response.ok().status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.notModified().build();
        }
    }
}