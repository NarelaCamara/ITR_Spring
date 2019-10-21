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

    @Autowired
    private SalvoRepository salvoRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @RequestMapping("/game_view/{nn}")
    public Map<String,Object> getPlayerAndShipsAndSalvoes(@PathVariable long nn) {
        Map<String, Object> dto = new LinkedHashMap<>();
        GamePlayer playerChosen = gamePlayerRepository.findById(nn).get();
        dto.put("id", playerChosen.getGame().getId());
        dto.put("created", playerChosen.getGame().getFechaDeCreacion());
        dto.put("gamePlayers", playerChosen.getGame().getGamePlayers()
                .stream().map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO()).collect(Collectors.toList()));
        dto.put("ships", playerChosen.getShips()
                .stream().map(ship -> ship.makeShipDTO()).collect(Collectors.toList()));
        dto.put("salvoes", playerChosen.getSalvoes()
                .stream().map(salvo -> salvo.makeSalvoDTO()).collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping("/games")
    public Map<String,Object> getAllAndScores() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player", "Guest");
         GameRepository gamesAll = gameRepository;
        dto.put("games", gamesAll.findAll().stream().map(game -> game.makeGameDTO() ));
        return dto;
    }
}
