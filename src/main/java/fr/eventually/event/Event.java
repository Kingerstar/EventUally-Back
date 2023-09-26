package fr.eventually.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.eventually.localization.Localization;
import fr.eventually.organization.Organization;
import fr.eventually.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String banner;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("eventList")
    private Localization localization;
    private List<String> categoryList;
    private String startingDate;
    private String endingDate;
    private Integer userMaxJoin;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("eventJoined")
    private Set<User> eventFollowedBy;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnoreProperties("eventList")
    private Organization organization ;
}
