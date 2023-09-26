package fr.eventually.bind;

import fr.eventually.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/bind")
@RequiredArgsConstructor
public class UserEventController {

    private final UserEventService service ;

    @GetMapping("/userId={userId}/eventId={eventId}")
    public Map<String, String> bindEventWithUser(
            @PathVariable("eventId") Long eventId,
            @PathVariable("userId") Long userId
    ){
        return service.bindEventWithUser(eventId, userId) ;
    }
}
