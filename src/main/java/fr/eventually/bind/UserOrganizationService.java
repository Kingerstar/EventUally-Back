package fr.eventually.bind;
import fr.eventually.organization.Organization;
import fr.eventually.organization.OrganizationRepository;
import fr.eventually.user.User;
import fr.eventually.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class UserOrganizationService {

    private final UserRepository userRepository ;
    private final OrganizationRepository organizationRepository;

    public Map<String, String> bindUserWithOrga(Long organizationId, Long userId    ) {
        Map<String, String> responseBody = new HashMap<>();

        User userfound = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User ID " + userId + " not found"));
        Organization organizationfound = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Orga ID " + organizationId + " not found"));

        Set<Organization> organizationFollowed = userfound.getOrganizationFollowed();

        boolean organizationExists = organizationFollowed.stream()
                .anyMatch(organization -> organization.getId().equals(organizationId));
        organizationFollowed.removeIf(organization -> organization.getId().equals(organizationId));

        if (!organizationExists) {
            organizationFollowed.add(organizationfound);
            responseBody.put("message", "Orga suivi \uD83E\uDD73");
        } else {
            responseBody.put("message", "Orga abandonné \uD83E\uDDF9");
        }
        responseBody.put("type", "work");

        userRepository.save(userfound);

        return responseBody;
    }}
