package classusers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @JsonView(View.General.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(View.General.class)
    private String firstname;

    @JsonView(View.General.class)
    private String lastname;

    @JsonView(View.General.class)
    private String email;


    @JsonView(View.User.class)
    @OneToMany(mappedBy = "creator")
    private Set<EClass> createdclasses = new HashSet<>();

    @JsonView(View.User.class)
    @ManyToMany(cascade=CascadeType.MERGE, mappedBy = "students")
    private Set<EClass> studiedclasses = new HashSet<>();

    private User() { } 

    public User(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<EClass> getCreatedClasses() {
        return createdclasses;
    }

    public void setCreatedClasses(Set<EClass> createdclasses) {
        this.createdclasses = createdclasses;
    }

    public Set<EClass> getStudiedclasses() {
        return studiedclasses;
    }

    public void setStudiedclasses(Set<EClass> studiedclasses) {
        this.studiedclasses = studiedclasses;
    }

}
