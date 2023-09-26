package fr.eventually.localization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.eventually.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Localization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer streetNumber;
    private String streetName;
    private String cityName;
    private String districtName;
    private List<Float> gpsCoordinates;
    @OneToMany
    @JoinColumn(name = "localization_id", referencedColumnName = "id")
    @JsonIgnoreProperties("localization")
    private Set<Event> eventList = new HashSet<>();
}