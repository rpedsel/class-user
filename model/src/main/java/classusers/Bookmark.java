package classusers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Bookmark {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne
    private User user;

    private String uri;

    private String description;

    private Bookmark() { } // JPA only

    public Bookmark(final User user, final String uri, final String description) {
        
        this.user = user;
        this.uri = uri;
        this.description = description;
    }

    public static Bookmark from(User user, Bookmark bookmark) {
        return new Bookmark(user, bookmark.uri, bookmark.getDescription());
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getUri() {
        return uri;
    }

    public String getDescription() {
        return description;
    }
}
