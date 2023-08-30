package com.nick.cards.domain.cards.services;

import com.nick.cards.appmodels.MessageResponse;
import com.nick.cards.appmodels.UserAuditModel;
import com.nick.cards.domain.cards.models.request.CardFilterRequest;
import com.nick.cards.domain.cards.models.request.CreateCardRequest;
import com.nick.cards.domain.cards.models.request.UpdateCardRequest;
import com.nick.cards.domain.cards.models.response.CardModel;
import com.nick.cards.domain.cards.repositories.CardRepository;
import com.nick.cards.domain.security.jwt.TokenExtractor;
import com.nick.cards.domain.security.repositories.RoleRepository;
import com.nick.cards.domain.security.repositories.UserRepository;
import com.nick.cards.entities.Card;
import com.nick.cards.entities.User;
import com.nick.cards.enums.ECardStatus;
import com.nick.cards.enums.ERole;
import com.nick.cards.exceptions.NotFoundException;
import com.nick.cards.exceptions.OperationNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
public class CardServiceImpl implements CardService {

  private final CardRepository cardRepository;
  private final TokenExtractor tokenExtractor;

  private final UserRepository userRepository;

  private final ModelMapper modelMapper;

  private final RoleRepository roleRepository;

  public CardServiceImpl(
          CardRepository cardRepository,
          TokenExtractor tokenExtractor,
          UserRepository userRepository,
          ModelMapper modelMapper, RoleRepository roleRepository) {
    this.cardRepository = cardRepository;
    this.tokenExtractor = tokenExtractor;
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
    this.roleRepository = roleRepository;
  }

  @Override
  public List<CardModel> fetchAllCards(HttpServletRequest request) {
    UserAuditModel userAuditModel = tokenExtractor.extractUserNameFromToken(request);
    User user =
        userRepository
            .findByEmail(userAuditModel.getCreatedBy())
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "User Not Found with username: " + userAuditModel.getCreatedBy()));
    List<Card> cards;
    if (user.getRoles().contains(ERole.ROLE_MEMBER.name())) {
      cards = cardRepository.findAll();
    } else {
      cards = cardRepository.findByOwner(user);
    }
    return getCardModels(cards.stream());
  }

  private List<CardModel> getCardModels(Stream<Card> stream) {
    return stream
        .map(
            card -> {
              CardModel model = new CardModel();
              model.setColor(card.getColor());
              model.setName(card.getName());
              model.setDescription(card.getDescription());
              model.setGuid(card.getGuid());
              model.setDateTimeCreated(card.getDateTimeCreated());
              return model;
            })
        .collect(Collectors.toList());
  }

  @Override
  public ResponseEntity<MessageResponse> save(
          @Valid CreateCardRequest createCardRequest, HttpServletRequest request) {
    UserAuditModel userAuditModel = tokenExtractor.extractUserNameFromToken(request);
    User user =
        userRepository
            .findByEmail(userAuditModel.getCreatedBy())
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "User Not Found with username: " + userAuditModel.getCreatedBy()));

    Card card = new Card();
    card.setColor(createCardRequest.getColor());
    card.setDescription(createCardRequest.getDescription());
    card.setName(createCardRequest.getName());
    card.setOwner(user);
    card.setStatus(ECardStatus.TODO.name());
    card.setCreatedBy(userAuditModel.getCreatedBy());
    card.setLastModifiedBy(userAuditModel.getLastModifiedBy());

    card = cardRepository.saveAndFlush(card);

    log.trace("Card saved  card guid: {}, creating card setting", card.getGuid());

    log.trace("Card created successfully {}", user.getGuid());
    return ResponseEntity.ok(
        MessageResponse.builder()
            .body(modelMapper.map(card, CardModel.class))
            .message("Card created successfully!")
            .build());
  }

  @Override
  public List<CardModel> filterCards(
      CardFilterRequest filterRequest, HttpServletRequest request, Pageable pageable) {
    UserAuditModel userAuditModel = tokenExtractor.extractUserNameFromToken(request);
    User user =
        userRepository
            .findByEmail(userAuditModel.getCreatedBy())
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "User Not Found with username: " + userAuditModel.getCreatedBy()));

     pageable =
        PageRequest.of(
            filterRequest.getPage(), filterRequest.getSize(), Sort.by((filterRequest.getSortBy() == null) ? "name" : filterRequest.getSortBy()));

    Page<Card> cards  =
          cardRepository.findByFilterCriteria(
              filterRequest.getName(),
              filterRequest.getColor(),
              filterRequest.getStatus(),
              filterRequest.getCreationDate(),
              pageable);

    return getCardModels(cards.stream());
  }

  @Override
  public ResponseEntity<MessageResponse> update(
          UpdateCardRequest cardModel, HttpServletRequest request) throws Exception {
    Card card =
        cardRepository
            .findByGuid(cardModel.getGuid())
            .orElseThrow(
                () -> new UsernameNotFoundException("Card does not exist: " + cardModel.getGuid()));

    UserAuditModel userAuditModel = tokenExtractor.extractUserNameFromToken(request);
    User user =
        userRepository
            .findByEmail(userAuditModel.getCreatedBy())
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "User Not Found with username: " + userAuditModel.getCreatedBy()));
    if (!user.getRoles().contains(ERole.ROLE_ADMIN)) {
      if (!card.getOwner().equals(user)) {
        throw new Exception("You are not allowed to edit this card");
      }
    }

    card.setColor(cardModel.getColor());
    cardModel.setDescription(card.getDescription());
    cardModel.setName(card.getName());
    card = cardRepository.saveAndFlush(card);

    log.trace("Card updated  card guid: {}, creating card setting", card.getGuid());

    log.trace("Card updated successfully {}", user.getGuid());
    return ResponseEntity.ok(
        MessageResponse.builder()
            .body(modelMapper.map(card, CardModel.class))
            .message("Card updated successfully!")
            .build());
  }

  @Override
  public ResponseEntity<MessageResponse> delete(UUID guid, HttpServletRequest request)
      throws Exception {
    UserAuditModel userAuditModel = tokenExtractor.extractUserNameFromToken(request);
    User user =
        userRepository
            .findByEmail(userAuditModel.getCreatedBy())
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "User Not Found with username: " + userAuditModel.getCreatedBy()));

    Card card =
        cardRepository
            .findByGuid(guid)
            .orElseThrow(() -> new NotFoundException("Card not found with guid: " + guid));

    if (!user.equals(card.getOwner())) {
      throw new OperationNotAllowedException("You are forbidden from deleting this card");
    }

    cardRepository.delete(card);
    log.trace("Card deleted  card guid: {}, creating card setting", card.getGuid());

    log.trace("Card deleted successfully {}", user.getGuid());
    return ResponseEntity.ok(
            MessageResponse.builder()
                    .body(modelMapper.map(card, CardModel.class))
                    .message("Card deleted successfully!")
                    .build());
  }

  @Override
  public ResponseEntity<MessageResponse> get(UUID guid, HttpServletRequest request) throws Exception {
    UserAuditModel userAuditModel = tokenExtractor.extractUserNameFromToken(request);
    User user =
            userRepository
                    .findByEmail(userAuditModel.getCreatedBy())
                    .orElseThrow(
                            () ->
                                    new UsernameNotFoundException(
                                            "User Not Found with username: " + userAuditModel.getCreatedBy()));

    Card card =
            cardRepository
                    .findByGuid(guid)
                    .orElseThrow(() -> new NotFoundException("Card not found with guid: " + guid));

    if (!user.equals(card.getOwner())) {
      throw new OperationNotAllowedException("You do not have access to this card");
    }

    return ResponseEntity.ok(
            MessageResponse.builder()
                    .body(modelMapper.map(card, CardModel.class))
                    .message("Card found!")
                    .build());
  }
}
