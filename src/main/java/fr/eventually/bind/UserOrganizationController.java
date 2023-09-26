package fr.eventually.bind;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bind")
@RequiredArgsConstructor
public class UserOrganizationController {
  private final UserOrganizationService service;

  @GetMapping("/userId={userId}/organizationId={organizationId}")
  public Map<String, String> bindUserWithOrga(
          @PathVariable("organizationId") Long organizationId,
          @PathVariable("userId") Long userId
  ){
    return service.bindUserWithOrga(organizationId, userId) ;
  }
}
