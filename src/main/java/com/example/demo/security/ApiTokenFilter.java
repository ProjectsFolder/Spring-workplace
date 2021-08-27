package com.example.demo.security;

import com.example.demo.classes.ApiErrorResponse;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class ApiTokenFilter extends GenericFilterBean {

    private final String token;

    public ApiTokenFilter(String token) {
        this.token = token;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var token = request.getParameter("token");
        if (!this.token.equals(token)) {
            response.setContentType("application/json");
            (new ApiErrorResponse("Invalid token.")).toOutput(response.getOutputStream());

            return;
        }
        chain.doFilter(request, response);
    }
}

//            var user = new User();
//            user.setUsername("api");
//            user.setRoles(Collections.singleton(new Role(2L, "ROLE_ADMIN")));
//            var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(auth);
