package fr.eventually.organization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;

    public List<Organization> getAll() {
        return repository.findAll();
    }

    public Organization getById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("id "+id+" has not be finded")
        );
    }

    public void add(Organization organization) {
        repository.save(organization);
    }

    public String[] update(Long id, Organization organization) {
        Organization orgaFounded = this.getById(id);
        orgaFounded.setName(organization.getName());
        orgaFounded.setLocalization(organization.getLocalization());
        orgaFounded.setPassword(organization.getPassword());
        orgaFounded.setCategoryList(organization.getCategoryList());
        orgaFounded.setEmail(organization.getEmail());
        orgaFounded.setDescription(organization.getDescription());
        orgaFounded.setWebsiteLink(organization.getWebsiteLink());
        repository.save(orgaFounded);
        return new String[]{"TEST TODO", "error"};
    }

    public String[] delete(Long id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return new String[]{"Bien supprimé !", "work"};
        }
        return new String[]{"Un soucis est survenu !", "error"};
    }

    public Long getQuantity() {
        return repository.findAll().stream().count();
    }
}
