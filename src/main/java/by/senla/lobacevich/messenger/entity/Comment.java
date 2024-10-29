package by.senla.lobacevich.messenger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "mes", name = "comments")
public class Comment implements AppEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Post post;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Profile author;

    @Column(nullable = false)
    private String textComment;

    @Column(nullable = false)
    private LocalDateTime createdDate;
}
