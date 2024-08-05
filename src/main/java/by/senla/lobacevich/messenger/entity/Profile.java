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
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(schema = "mes", name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @Column(length = 23)
    private String firstname;

    @Column(length = 23)
    private String lastname;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToMany(mappedBy = "participants")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Group> ownedGroups = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Chat> chats = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<RequestFriendship> sendFriendRequests = new HashSet<>();

    @OneToMany(mappedBy = "recipient")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<RequestFriendship> receiveFriendRequests = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();
}
