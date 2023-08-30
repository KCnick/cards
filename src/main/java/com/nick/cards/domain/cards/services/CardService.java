package com.nick.cards.domain.cards.services;

import com.nick.cards.appmodels.MessageResponse;
import com.nick.cards.domain.cards.models.request.CardFilterRequest;
import com.nick.cards.domain.cards.models.request.CreateCardRequest;
import com.nick.cards.domain.cards.models.request.UpdateCardRequest;
import com.nick.cards.domain.cards.models.response.CardModel;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface CardService {
    List<CardModel> fetchAllCards(HttpServletRequest request);
    ResponseEntity<MessageResponse> save(CreateCardRequest createCardRequest, HttpServletRequest request);
    List<CardModel> filterCards(CardFilterRequest cardFilterRequest, HttpServletRequest request, Pageable pageable);
    ResponseEntity<MessageResponse> update(UpdateCardRequest cardModel, HttpServletRequest request) throws Exception;
    ResponseEntity<MessageResponse> delete(UUID guid, HttpServletRequest request) throws Exception;

    ResponseEntity<MessageResponse> get(UUID guid, HttpServletRequest request) throws Exception;


}
