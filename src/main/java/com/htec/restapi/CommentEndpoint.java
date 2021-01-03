package com.htec.restapi;

import com.htec.model.Comment;
import com.htec.service.ICommentService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/comments")
public class CommentEndpoint {

    @Inject
    ICommentService commentService;

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/get-comments")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComments() {

        List<Comment> comments = commentService.findAll();

        if (!comments.isEmpty()) {

            return Response.ok(comments).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/find-comment/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findComment(@PathParam("id") Long id) {

        Comment comment = commentService.find(id);

        if (comment.getIdComment() != null) {
            return Response.ok(comment).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/save-comment")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response saveComment(@FormParam("comment") String commentMessage,
                             @FormParam("cityid") Long cityid) {

        Comment comment = Comment.builder()
                .comment(commentMessage)
                .fkCityId(cityid).build();

        boolean r = commentService.save(comment);

        if (r) {
            return Response.ok().status(Response.Status.CREATED).build();
        } else {
            return Response.notModified().build();
        }
    }

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/update-comment/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateComment(@FormParam("comment") String commentMessage,
                               @FormParam("cityid") Long cityid,
                               @PathParam("id") Long id) {

        Comment comment = Comment.builder()
                .comment(commentMessage)
                .fkCityId(cityid).build();

        boolean r = commentService.update(comment, id);

        if (r) {
            return Response.ok().status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.notModified().build();
        }
    }

    @RolesAllowed({"ADMIN","REGULAR"})
    @Path("/delete-comment/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteComment(@PathParam("id") Long id) {

        boolean r = commentService.delete(id);

        if (r) {
            return Response.ok().status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.notModified().build();
        }
    }
}
