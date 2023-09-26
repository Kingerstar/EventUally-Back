package fr.eventually.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/events")
public class EventController {

    private final EventService service;

    @GetMapping
    public List<Event> getAll(){
        return this.service.getAll();
    }

    @GetMapping("/{idName}")
    public Event getById(@PathVariable String idName){
        return this.service.getById(idName);
    }

    @GetMapping("/week")
    public List<Event> getWeek(){
        return this.service.getWeek();
    }

    @GetMapping("/quantity")
    public Long getQuantity(){
        return service.getQuantity();
    }

    @PostMapping(path = "/add")
    public void add(@RequestBody Event event){
        this.service.add(event);
    }

    @PutMapping(path = "/update/{idName}")
    public void update(@PathVariable String idName, @RequestBody Event event){
        this.service.update(idName, event);
    }

    @DeleteMapping(path = "/delete/{id}")
    public String[] delete(@PathVariable("id") Long id){
        return this.service.delete(id);
    }
}
