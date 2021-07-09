package com.atul.sportsmanagement.mapper;

import com.atul.sportsmanagement.dto.WinnersRequest;
import com.atul.sportsmanagement.dto.WinnersResponse;
import com.atul.sportsmanagement.model.Event;
import com.atul.sportsmanagement.model.Winners;
import org.springframework.stereotype.Component;

@Component
public class WinnersMapper {
    public static WinnersResponse mapToDto(Winners winners) {
        WinnersResponse winnersResponse = new WinnersResponse();
        winnersResponse.setW1(winners.getWinner());
        winnersResponse.setW2(winners.getFirstRunnerUp());
        winnersResponse.setW3(winners.getSecondRunnerUp());
        return winnersResponse;
    }

    public Winners map(WinnersRequest winnersRequest, Long id, Event event){
        Winners winners = new Winners();
        winners.setEventId(id);
        winners.setWinner(winnersRequest.getW1());
        winners.setFirstRunnerUp(winnersRequest.getW2());
        winners.setSecondRunnerUp(winnersRequest.getW3());
        winners.setEvent(event);
        return winners;
    }
}
