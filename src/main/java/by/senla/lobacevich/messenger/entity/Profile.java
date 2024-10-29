package by.senla.lobacevich.messenger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(schema = "mes", name = "profiles")
public class Profile implements AppEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 23)
    private String firstname;

    @Column(length = 23)
    private String lastname;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "participants")
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<Chat> chats = new HashSet<>();

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private Set<Group> ownedGroups = new HashSet<>();

    @OneToMany(mappedBy = "sender", orphanRemoval = true)
    private Set<RequestFriendship> sendFriendRequests = new HashSet<>();

    @OneToMany(mappedBy = "recipient", orphanRemoval = true)
    private Set<RequestFriendship> receiveFriendRequests = new HashSet<>();

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Set<Message> messages = new HashSet<>();

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Profile profile = (Profile) object;
        return Objects.equals(id, profile.id) && Objects.equals(firstname, profile.firstname) && Objects.equals(lastname, profile.lastname) && Objects.equals(createdDate, profile.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, createdDate);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }

    @PreRemove
    private void onRemove() {
        groups.forEach(group -> group.getParticipants().remove(this));
        chats.forEach(chat -> chat.getParticipants().remove(this));
    }
}
