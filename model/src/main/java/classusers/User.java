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
    @GeneratedValue
    private Long id;

    //private String username;
    @JsonView(View.General.class)
    private String firstname;

    @JsonView(View.General.class)
    private String lastname;

    @JsonView(View.General.class)
    private String email;

    //@JsonIgnore
    //private String password;

    //******* set to list? *******//
    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    private Set<EClass> eclasses = new HashSet<>();

    //@JsonIgnore
    //@JsonView(View.General.class)
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "students")
    private Set<EClass> studiedclasses = new HashSet<>();

    private User() { } // JPA only

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

    public void setLastname(String Lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<EClass> getEClasses() {
        return eclasses;
    }

    public void setEClasses(Set<EClass> eclasses) {
        this.eclasses = eclasses;
    }

    public Set<EClass> getStudiedclasses() {
        return eclasses;
    }

    public void setStudiedclasses(Set<EClass> eclasses) {
        this.eclasses = eclasses;
    }

}
