package com.htec.auth;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.htec.dao.IUserDao;
import com.htec.model.User;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.util.Base64;

/**
 * This interceptor verify the access permissions for a user
 * based on username and password provided in request
 */
@Provider
public class SecurityInterceptor implements javax.ws.rs.container.ContainerRequestFilter {
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401, new Headers<Object>());

    private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nobody can access this resource", 403, new Headers<Object>());

    private static final ServerResponse SERVER_ERROR = new ServerResponse("INTERNAL SERVER ERROR", 500, new Headers<Object>());

    @Inject
    IUserDao userDao;

    /**
     * Execute access to required roles for accessed par of API endpoint. Beside this provides username and password for
     * user that is currently accessing system.
     *
     * @param requestContext through which access to invoked function with required role set is provided.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();

        if (!method.isAnnotationPresent(PermitAll.class)) {

            if (method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }

            final MultivaluedMap<String, String> headers = requestContext.getHeaders();

            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

            if (authorization == null || authorization.isEmpty()) {
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }

            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

            String usernameAndPassword = null;
            try {
                usernameAndPassword = new String(Base64.decode(encodedUserPassword));
            } catch (IOException e) {
                requestContext.abortWith(SERVER_ERROR);
                return;
            }

            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            final String username = tokenizer.nextToken();
            final String password = tokenizer.nextToken();

            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                if (!isUserAllowed(username, password, rolesSet)) {
                    requestContext.abortWith(ACCESS_DENIED);
                    return;
                }
            }
        }
    }

    /**
     * Performs authentication and authorization of currently logged user on API. If user can be found in database
     * with required username and password, and if he is registered, user will be allowed to access system via API.
     *
     * @param username provided username via basic authentication
     * @param password provided password via basic authentication
     * @param rolesSet provided set of roles for function inside API endpoint
     * @return boolean flag if user is allowed to use particular part for accessed endpoint
     */
    private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet) {
        boolean isAllowed = false;

        User user = userDao.findRoleByUsername(username);

        if (rolesSet.contains(user.getRole()) && password.equals(user.getPassword()) && user.getRegistered() == 1) {
            isAllowed = true;
        }
        return isAllowed;
    }
}