package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShipRepository shipRepository;


    @RequestMapping("/games")
    public List<Map<String,Object>> getAll() {
        return gameRepository.findAll().stream().map(game -> game.makeGameDTO()).collect(Collectors.toList());
        }

    @RequestMapping("/game_view/{nn}")
    public Map<String,Object> getPlayerID(@PathVariable long nn) {
        Map<String, Object> dto = new LinkedHashMap<>();
      GamePlayer playerChosen= gamePlayerRepository.findById(nn).get();

      dto.put( "id", playerChosen.getGame().getId());

      dto.put( "created", playerChosen.getGame().getFechaDeCreacion());

      dto.put( "gamePlayers", playerChosen.getGame().getGamePlayers()
      .stream().map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO()).collect(Collectors.toList()));

      dto.put( "ships", playerChosen.getShips()
      .stream().map(ship -> ship.makeShipDTO()).collect(Collectors.toList()));

      return dto;
    }


}
