package fr.eventually.auth;

import fr.eventually.user.User;
import fr.eventually.user.UserRepository;
import fr.eventually.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Map<String, String> register(RegisterRequest request, HttpServletRequest httpRequest) throws Exception {

        if (!repository.findByEmail(request.getEmail()).isPresent()) {
            var user = User.builder()
                    .pseudo(request.getPseudo())
                    .firstname("")
                    .lastname("")
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .profilePicture("assets/images/default-profile.png")
                    .introduction("Quelques mots sur moi...")
                    .role("ROLE_USER")
                    .build();
            if(request.getEmail().equals("admin@admin")){
                user = User.builder()
                        .pseudo("ADMIN")
                        .firstname("")
                        .lastname("")
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .profilePicture("assets/images/admin.jpg")
                        .introduction("Je suis l'un des administrateurs du site.")
                        .role("ROLE_ADMIN")
                        .build();
            }

            repository.save(user);

            Map<String, String> body = new HashMap<>();
            body.put("message", "Inscription validée \uD83C\uDF89");
            body.put("type", "work");
            return body;

        } else {
            httpRequest.setAttribute("username_taken_exception", "Username already taken");
            throw new Exception("Username already taken");
        }

    }

    public AuthResponse authenticate(AuthRequest request, HttpServletRequest httpRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = repository.findByEmail(request.getEmail()).orElseThrow();

            /* On extrait le rôle de l'utilisateur */
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("role", user.getRole().toString());

            /* On génère le token avec le rôle */
            String jwtToken = jwtService.generateToken(new HashMap<>(extraClaims), user);
            return AuthResponse.builder()
                    .token(jwtToken)
                    .message("Bienvenue \uD83D\uDC4B")
                    .build();

        } catch (BadCredentialsException ex) {
            httpRequest.setAttribute("bad_credentials", ex.getMessage());
            throw new BadCredentialsException("Bad credentials");
        } catch (AuthenticationException e) {
            httpRequest.setAttribute("auth_err", "Authentication error");
            throw e;
        }


    }
}
