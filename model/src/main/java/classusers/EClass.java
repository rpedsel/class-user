package classusers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class EClass {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne
    private User creator;

    // @OneToMany(mappedBy = "eclass")
    // private Set<User> students = new HashSet<>();

    private String classname;

    //private String description;

    private EClass() { } // JPA only

    public EClass(final User creator, final String classname) {    
        this.creator = creator;
        this.classname = classname;
    }

    // not used?
    public static EClass from(User creator, EClass eclass) {
        return new EClass(creator, eclass.classname);
    }

    public Long getId() {
        return id;
    }

    public User getCreator() {
        return creator;
    }

    public String getClassname() {
        return classname;
    }

}
