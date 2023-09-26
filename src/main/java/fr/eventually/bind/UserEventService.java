package fr.eventually.bind;

import fr.eventually.event.Event;
import fr.eventually.event.EventRepository;
import fr.eventually.organization.Organization;
import fr.eventually.user.User;
import fr.eventually.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserEventService {

    private final UserRepository userRepository ;

    private final EventRepository eventRepository;

    public Map<String, String> bindEventWithUser(Long eventId, Long userId) {

        Map<String, String> responseBody = new HashMap<>();

        User userfound = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User ID " + userId + " not found"));
        Event eventfound = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event ID " + eventId + " not found"));

        Set<Event> eventJoined = userfound.getEventJoined();

        boolean eventExists = eventJoined.stream()
                .anyMatch(event -> event.getId().equals(eventId));
        eventJoined.removeIf(event -> event.getId().equals(eventId));

        if (!eventExists) {
            eventJoined.add(eventfound);
            responseBody.put("message", "Évènement rejoint \uD83E\uDD73");
        }else{
            responseBody.put("message", "Évènement abandonné \uD83E\uDDF9");
        }
        responseBody.put("type", "work");

        userRepository.save(userfound);

        return responseBody;
    }


}
