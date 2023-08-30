package com.nick.cards.controllers;

import com.nick.cards.appmodels.MessageResponse;
import com.nick.cards.domain.cards.models.request.CardFilterRequest;
import com.nick.cards.domain.cards.models.request.CreateCardRequest;
import com.nick.cards.domain.cards.models.request.UpdateCardRequest;
import com.nick.cards.domain.cards.models.response.CardModel;
import com.nick.cards.domain.cards.services.CardService;
import com.nick.cards.domain.cards.services.CardServiceImpl;
import com.nick.cards.utils.constants.AppConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(
    value = AppConstant.BASE_URL + "/cards",
    produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Card")
public class CardController {

  private final CardService cardService;

  public CardController(CardServiceImpl cardService) {
    this.cardService = cardService;
  }

  @PostMapping("/create")
  @ApiOperation(
      value = "Create",
      notes = "Create a new card returns MessageResponse model with status of request",
      response = MessageResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Ok", response = MessageResponse.class),
      })
  public ResponseEntity<MessageResponse> createCard(
          @Valid @RequestBody CreateCardRequest createCardRequest, HttpServletRequest request) {

    return cardService.save(createCardRequest, request);
  }

  @PostMapping("/update")
  @ApiOperation(
          value = "Update",
          notes = "Update a card returns MessageResponse model with status of request",
          response = MessageResponse.class)
  @ApiResponses(
          value = {
                  @ApiResponse(code = 200, message = "Ok", response = MessageResponse.class),
          })
  public ResponseEntity<MessageResponse> updateCard(
          @Valid @RequestBody UpdateCardRequest createCardRequest, HttpServletRequest request) throws Exception {

    return cardService.update(createCardRequest, request);
  }

  @PostMapping("/filter")
  @ApiOperation(
      value = "Filter cards",
      notes = "Filter cards. Returns list of CardModel matching criteria",
      response = MessageResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Ok", response = CardModel.class),
      })
  public ResponseEntity<List<CardModel>> filterCards(
          @RequestBody CardFilterRequest cardFilterRequest, HttpServletRequest request,  @PageableDefault(size = 10) Pageable pageable) {

    return ResponseEntity.ok(cardService.filterCards(cardFilterRequest, request, pageable));
  }

  @GetMapping("/{guuid}")
  @ApiOperation(
      value = "Get card",
      notes = "Returns CardModel matching criteria",
      response = MessageResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Ok", response = CardModel.class),
      })
  public ResponseEntity<MessageResponse> get(
          @PathVariable UUID guuid, HttpServletRequest request) throws Exception {

    return cardService.get(guuid, request);
  }

  @DeleteMapping("/{guuid}")
  @ApiOperation(
          value = "Get card",
          notes = "Returns CardModel matching criteria",
          response = MessageResponse.class)
  @ApiResponses(
          value = {
                  @ApiResponse(code = 200, message = "Ok", response = CardModel.class),
          })
  public ResponseEntity<MessageResponse> delete(
          @PathVariable UUID guuid, HttpServletRequest request) throws Exception {

    return cardService.delete(guuid, request);
  }
}
