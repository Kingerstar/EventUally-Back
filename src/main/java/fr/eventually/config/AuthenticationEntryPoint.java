package fr.eventually.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    // Cette class permet de renvoyer au FRONT les erreurs liées aux problèmes d'authentification
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("error", exception.getMessage());

        Map<String, String> error = new HashMap<>();


        if (request.getAttribute("expired_exception") != null) {
            error.put("is_token_expired", "true");
            error.put("error_message", "La session a expirée, reconnecte toi \uD83D\uDCE1");
        } else if (request.getAttribute("malformed_exception") != null) {
            error.put("is_jwt_malformed", "true");
            error.put("error_message", "JWT is malformed. Please verify its integrity.");
        } else if (request.getAttribute("jwt_exception") != null) {
            error.put("is_jwt_exception", "true");
            error.put("error_message", "JWT exception.");
        } else if (request.getAttribute("username_taken_exception") != null) {
            error.put("is_username_taken", "true");
            error.put("error_message", "Email déjà pris \uD83E\uDD14");
        } else if (request.getAttribute("bad_credentials") != null) {
            error.put("bad_credentials", "true");
            error.put("error_message", "Le compte n'existe pas, ou le mot de passe n'est pas le bon \uD83D\uDD10");
        } else if (request.getAttribute("auth_err") != null) {
            error.put("auth_err", "true");
            error.put("error_message", "Une erreur d'authentification s'est produite \uD83E\uDEAA");
        } else if (request.getAttribute("no_jwt_provided") != null) {
            error.put("no_jwt_provided", "true");
            error.put("error_message", "No JWT provided.");
        } else if (request.getAttribute("access_denied") != null){
            error.put("access_denied", "true");
            error.put("error_message", "Accès refusé ✋");
        }

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
