package com.nick.cards.domain.cards.repositories;

import com.nick.cards.entities.Card;
import com.nick.cards.entities.User;
import com.nick.cards.enums.ECardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

  List<Card> findByOwner(User user);

  @Query(
      "SELECT c FROM Card c WHERE (:name is null OR c.name LIKE %:name%) "
          + "AND (:color is null OR c.color = :color) "
          + "AND (:status is null OR c.status = :status) "
          + "AND (:creationDate is null OR c.dateTimeCreated = :creationDate)")
  Page<Card> findByFilterCriteria( @Param("name") String name,
                                   @Param("color") String color,
                                   @Param("status") String status,
                                   @Param("creationDate") LocalDate creationDate,
                                  Pageable pageable);

  Optional<Card> findByGuid(UUID guid);
}
