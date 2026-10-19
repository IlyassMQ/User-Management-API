package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.entity.Enum.Role;
import org.example.entity.User;
import org.example.exception.LoginRequired;
import org.example.service.UserService;

import java.io.IOException;
import java.util.UUID;


public class Filter implements jakarta.servlet.Filter {

    private final UserService userService;

    public Filter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String path = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
        String methode = httpServletRequest.getMethod();

        if (path.equals("/api/auth/login") && methode.equals("POST")) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            throw new LoginRequired();
        }
        UUID userId = (UUID) session.getAttribute("id");
        User user = userService.findById(userId);
        boolean authorized = false;

        if (path.equals("/api/users/me") && (user.getRole().equals(Role.USER) || user.getRole().equals(Role.ADMIN))){
            authorized = true;
        }else if (path.equals("/api/auth/logout")) {
            authorized = true;
        } else if (path.equals("/api/users") && (user.getRole().equals(Role.ADMIN))) {
            authorized = true;
        }

        if (!authorized) {
            httpServletResponse.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied"
            );
            return;
        }

        chain.doFilter(request, response);
    }
}
