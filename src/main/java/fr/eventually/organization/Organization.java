package fr.eventually.organization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.eventually.event.Event;
import fr.eventually.localization.Localization;
import fr.eventually.user.User;
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
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("organization")
    private Localization localization;
    private String password;
    private String logo;
    private List<String> categoryList;
    private String email;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private String websiteLink;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("organizationFollowed")
    private Set<User> followedBy;
    @OneToMany
    @JoinColumn(name="organization_id",referencedColumnName = "id")
    @JsonIgnoreProperties("organization")
    private Set<Event> eventList = new HashSet<>() ;
}
