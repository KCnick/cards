package com.nick.cards.entities;

import com.nick.cards.enums.ECardStatus;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "Cards")
@Getter
@Setter
@ToString
@Accessors(chain = true)
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Card extends Auditable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "guid",columnDefinition = "VARCHAR(36)", nullable = false, unique = true, updatable = false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID guid = UUID.randomUUID();

    @NotBlank
    @Column(name = "name", length = 250, nullable = false,unique = true)
    private String name;

    @Column(name = "color", length = 7)
    private String color;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(length = 20, unique = true, name = "status")
    private String status;

    @ManyToOne
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return Objects.equals(id, card.id)
                && Objects.equals(guid, card.guid)
                && Objects.equals(name, card.name)
                && Objects.equals(color, card.color)
                && Objects.equals(description, card.description)
                && status == card.status
                && Objects.equals(owner, card.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guid, name, color, description, status, owner);
    }
}
