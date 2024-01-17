package SoloProject.SocialMediaApp;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

public class CustomTokenAuthenticationFilter implements Filter {

    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;

    public CustomTokenAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader("Authorization");

        String path = httpRequest.getRequestURI();
        System.out.println("Request Path: " + path);

        if (path.equals("/api/login")
                || path.equals("/api/account/createAccount")
                || path.equals("/api/publicFeed")
                || path.equals("/api/user/getAllWebsiteUsers")
                || path.matches("/api/account/.*/getProfilePicture")
                || path.matches("/api/.*/comments")
                || path.equals("/api/post/.*/comments")
                || path.endsWith("/logout")
                || path.endsWith("/images")
                || path.equals("/error")
                || path.equals("/api/account/ForgotPassword")
                || path.matches("/api/user/.*")
                || path.matches("/api/posts/.*")
                || path.matches("/api/user/.*/.*")
                || path.matches("/api/user/.*")
                || path.matches("/api/status/getByUser/.*")
                || path.matches("/api/.*/following")
                || path.matches("/api/.*/followers")
        ) {
            System.out.println("Bypassing JWT auth requirement");
            chain.doFilter(request, response);
            return;
        }

        try {
            if (authToken != null && !authToken.isEmpty() && authToken.startsWith("Bearer ")) {
                authToken = authToken.substring(7);
                System.out.println("Received Token:" + authToken);
                String username = jwtUtil.extractUsername(authToken);
                System.out.println("Username: " + username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.isTokenExpired(authToken)) {
                    unauthorizedResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired.");
                    return;
                }

                if (jwtUtil.validateToken(authToken, userDetails)) {
                    System.out.println("JWT valid. Username: " + username + ". Proceeding with the request.");

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    chain.doFilter(request, response);
                } else {
                    System.out.println("JWT invalid. Expected token for user: " + username);
                    unauthorizedResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT invalid. Unauthorized access attempt.");
                }
            } else {
                unauthorizedResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing. Unauthorized access attempt.");
            }
        } catch (UsernameNotFoundException e) {
            unauthorizedResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found. Unauthorized access attempt.");
        }
    }

    private void unauthorizedResponse(ServletResponse response, int statusCode, String message) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(statusCode);
        httpResponse.getWriter().write(message);
    }

    @Override
    public void destroy() {

    }
}