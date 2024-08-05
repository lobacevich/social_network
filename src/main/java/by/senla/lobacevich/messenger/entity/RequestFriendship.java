package by.senla.lobacevich.messenger.entity;

import by.senla.lobacevich.messenger.entity.enums.RequestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "mes", name = "requests_friendship",
        uniqueConstraints = @UniqueConstraint(columnNames =
        {"sender_id", "recipient_id"}))
public class RequestFriendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Profile sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Profile recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private RequestStatus status;
}
