package fr.eventually.organization;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService service;

    @GetMapping("/all")
    public List<Organization> getAll(){
        return service.getAll();
    }

    @GetMapping("/quantity")
    public Long getQuantity(){
        return service.getQuantity();
    }

    @GetMapping("/{id}")
    public Organization getById(@PathVariable("id") Long id){
        return service.getById(id);
    }

    @PostMapping("/add")
    public void add(@RequestBody Organization organization){
        service.add(organization);
    }

    @PutMapping("/update/{id}")
    public String[] update(@PathVariable("id") Long id, @RequestBody Organization organization){
        return service.update(id, organization);
    }

    @DeleteMapping("/delete/{id}")
    public String[] delete(@PathVariable Long id){
        return service.delete(id);
    }
}
