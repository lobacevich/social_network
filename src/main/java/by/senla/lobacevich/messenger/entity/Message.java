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
@Table(schema = "mes", name = "messages")
public class Message implements AppEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Profile author;
}
