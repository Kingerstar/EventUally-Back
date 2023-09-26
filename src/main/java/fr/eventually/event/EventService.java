package fr.eventually.event;

import fr.eventually.organization.Organization;
import fr.eventually.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EventService {

    private final OrganizationService organizationService ;

    private final EventRepository repository;

    public List<Event> getAll() {
        List<Event> all = repository.findAll();
        List<Event> currentAndNextEvent = new ArrayList<Event>();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        for(int i = 0; i < all.size(); i++){
            LocalDateTime dateOfCurrentEvent = LocalDateTime.parse( all.get(i).getEndingDate(), formatter);
            if(dateOfCurrentEvent.isAfter(now)){
                currentAndNextEvent.add(all.get(i));
            }
        }
        return currentAndNextEvent;
    }

    public Event getById(String idName) {
        System.out.println("idName: "+idName);
        Pattern p = Pattern.compile("^[0-9]+");
        Matcher m = p.matcher(idName);
        if(m.find()){
            Long id = Long.parseLong(m.group(0)); // 0: first return
            Event eventFound = repository.findById(id).orElseThrow(
                    () -> new RuntimeException("Event ID: "+id+" has not been founded")
            );
            // TODO check entire idName, not only id
            return eventFound;
        }
        return repository.findById(0L).orElseThrow(
                () -> new RuntimeException("Bad url structure, need to start with id")
        );
    }

    public List<Event> getWeek() {
        List<Event> all = this.getAll();
        List<Event> selected = new ArrayList<>();
        List<List<Event>> eventListByDay = new ArrayList<>(7);
        for(int day = 0; day < 7; day ++){
            eventListByDay.add(new ArrayList<Event>());
        }

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Calendar nowCalendar = Calendar.getInstance();
        Integer nowDay = nowCalendar.get(Calendar.DAY_OF_YEAR);

        for(int i = 0; i < all.size(); i++){
            try {
                Date date = formatter.parse(all.get(i).getStartingDate());
                Calendar eventCalendar = Calendar.getInstance();
                eventCalendar.setTime(date);
                Integer eventDay = eventCalendar.get(Calendar.DAY_OF_YEAR);
                if(eventDay - nowDay >= 0 && eventDay - nowDay <= 6){
                    eventListByDay.get(eventDay - nowDay).add(all.get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for(int dayNumber = 0; dayNumber <= 6; dayNumber ++){
            /*for(int eventOfDay = 0; eventOfDay < eventListByDay.get(dayNumber).size(); eventOfDay ++){
                System.out.println(eventListByDay.get(dayNumber).get(eventOfDay));
            }*/// TODO
            if(eventListByDay.get(dayNumber).size() > 0){
                selected.add(eventListByDay.get(dayNumber).get(0));// Get the first event (as random)
            }else{
                selected.add(new Event());
            }
        }

        return selected;
    }

    public void add(Event event) {
        Long orgaId = event.getOrganization().getId();
        Organization organization = organizationService.getById(orgaId) ;
        event.setOrganization(organization);
        repository.save(event);
    }

    public void update(String idName, Event event) {
        Event workingEvent = this.getById(idName);
        workingEvent.setName(event.getName());
        workingEvent.setDescription(event.getDescription());
        //workingEvent.setLocalization(event.getLocalization());
        workingEvent.setCategoryList(event.getCategoryList());
        workingEvent.setStartingDate(event.getStartingDate());
        workingEvent.setEndingDate(event.getEndingDate());
        workingEvent.setUserMaxJoin(event.getUserMaxJoin());
        repository.save(workingEvent);
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
