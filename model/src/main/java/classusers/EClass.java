package classusers;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class EClass {

    @JsonView(View.General.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(View.General.class)
    private String classname;

    @JsonView(View.Class.class)
    @ManyToOne
    private User creator;

    @JsonView(View.Student.class)
    @ManyToMany(cascade=CascadeType.MERGE)
    private Set<User> students = new HashSet<>();

    private EClass() { } 

    public EClass(User creator, String classname) {    
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

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public Set<User> getStudents() {
        return students;
    }

    public void setStudents(Set<User> students) {
        this.students = students;
    }
}
