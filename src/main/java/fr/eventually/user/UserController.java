package fr.eventually.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    private final UserService service;

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email, HttpServletRequest request) throws AccessDeniedException {
        String username  = SecurityContextHolder.getContext().getAuthentication().getName();
        String role  = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        if (username.equals(email) || role.equals("[ROLE_ADMIN]")) {
            return ResponseEntity.ok(userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("email " + email +" not found"))
            );
        } else {
            request.setAttribute("access_denied", "You do not have suffisant rights to access to this resource");
            throw new AccessDeniedException("User does not have the correct rights to access to this resource");
        }
    }

    @GetMapping("/id/{id}")
    public User getById(@PathVariable Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User ID: "+id+" has not be founded")
                // TODO return exception
        );
    }

    @GetMapping("/all")
    public List<User> getAll(HttpServletRequest request) throws AccessDeniedException {
        String role  = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if(role.equals("[ROLE_ADMIN]")) {
            return userRepository.findAll();
        } else {
            request.setAttribute("access_denied", "You do not have suffisant rights to access to this resource");
            throw new AccessDeniedException("User does not have the correct rights to access to this resource");
        }
    }

    @GetMapping("/quantity")
    public Long getQuantity(){
        return service.getQuantity();
    }

    /*@GetMapping("/id/{email}")
    public Long getId(@PathVariable String email){
        System.out.println("HERE-"+email+userRepository.findByEmail(email).get().getId());
        return userRepository.findByEmail(email).get().getId();
    }*/

    @GetMapping("/profilePicture/{id}")
    public Map<String,String> getProfilePicture(@PathVariable Long id){
        String profilePicture = userRepository.findById(id).get().getProfilePicture();
        Map<String, String> jsonReturned = new HashMap<>();
        jsonReturned.put("property",profilePicture);
        return jsonReturned;
    }

    @PutMapping("/update/introduction/{urlId}")
    public void updateIntroductionById(@PathVariable Long urlId, @RequestBody String newIntroduction){
        String username  = SecurityContextHolder.getContext().getAuthentication().getName();
        String role  = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        Long dataBaseId = userRepository.findByEmail(username).orElseThrow(
                () -> new RuntimeException("The email is not in the data base")
        ).getId();

        if (urlId == dataBaseId || role.equals("[ROLE_ADMIN]")) {
            User userFound = this.getById(urlId);
            userFound.setIntroduction(newIntroduction);
            userRepository.save(userFound);
        } else {
            //request.setAttribute("access_denied", "You do not have suffisant rights to access to this resource");
            throw new AccessDeniedException("User does not have the correct rights to access to this resource");
        }
    }

}