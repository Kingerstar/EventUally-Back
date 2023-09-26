package fr.eventually.user;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> getAll() {
        return repository.findAll();
    }

    public User getById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Id "+id+" has not be found")
        );
    }

    public void add(User organization) {
        repository.save(organization);
    }

    public void update(Long id, User user) {
       User userFounded = this.getById(id);
       userFounded.setFirstname(user.getFirstname());
       userFounded.setLastname(user.getLastname());
       userFounded.setEmail(user.getEmail());
       userFounded.setPassword(user.getPassword());
       //userFounded.setCategoryOfInterest(user.getCategoryOfInterest());
       userFounded.setProfilePicture(user.getProfilePicture());
       repository.save(userFounded);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }


    public Long getQuantity() {
        return repository.findAll().stream().count();
    }
}