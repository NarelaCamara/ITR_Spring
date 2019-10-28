package com.codeoftheweb.salvo.Controllers;

import com.codeoftheweb.salvo.ClassModel.Game;
import com.codeoftheweb.salvo.ClassModel.GamePlayer;
import com.codeoftheweb.salvo.ClassModel.Player;
import com.codeoftheweb.salvo.ClassModel.Ship;
import com.codeoftheweb.salvo.Interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private PasswordEncoder passwordEncoder;


    ///////////////////////////////////////////////////////////////////////////////////
    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    /////////////////////////////METODO PARA HACER UN USUARIO INCOGNITO/NO REGISTRADO //////////////////////////////////////
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(Authentication authentication) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        //Busco el player que creo el game
        Player player = playerRepository.findByEmail(authentication.getName());

        //Creo el game y lo guardo
        Game gameCreated = new Game(LocalDateTime.now());
        gameRepository.save(gameCreated);

        //Creo el gamePlayer y lo guardo en gamePlayer
        GamePlayer gamePlayerCreated = new GamePlayer(player, gameCreated);
        gamePlayerRepository.save(gamePlayerCreated);
        return new ResponseEntity<>(makeMap("gpid", gamePlayerCreated.getId()), HttpStatus.ACCEPTED);
    }

    /////////////////////////////////////////////////////////
    @RequestMapping("/games")
    public Map<String, Object> player(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (isGuest(authentication)) {
            dto.put("player", "Guest");
        } else {
            Player player = playerRepository.findByEmail(authentication.getName());
            dto.put("player", player.makePlayerDTO());
        }
        dto.put("games", gameRepository.findAll().stream().map(game -> game.makeGameDTO()).collect(Collectors.toList()));
        return dto;
    }

///////////////////////////////////////////////////////////////////////////////////////

    @RequestMapping(path = "/game_view/{id}")
    public ResponseEntity<Object> gamePlayerDelLogeado(Authentication authentication, @PathVariable Long id) {

        GamePlayer gamePlayerChosen = gamePlayerRepository.findById(id).orElse(null);
        Player player = playerRepository.findByEmail(authentication.getName());

        if (player == null || gamePlayerChosen == null) {
            return new ResponseEntity<>(makeMap("error", "Problemas o eres un player o gameplayer inexistente"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayerChosen.getPlayer().getId() != player.getId()) {
            return new ResponseEntity<>(makeMap("error", "No seas tramposo, eres un player")
                    , HttpStatus.UNAUTHORIZED);
        }
        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> hits = new LinkedHashMap<>();

        //Esto lo hice para que no me de error
        List<Object> listaVacia = new ArrayList();
        hits.put("self", listaVacia);
        hits.put("opponent", listaVacia);


        dto.put("id", gamePlayerChosen.getGame().getId());
        dto.put("created", gamePlayerChosen.getGame().getFechaDeCreacion());
        dto.put("gameState", "PLACESHIPS");
        dto.put("gamePlayers", gamePlayerChosen.getGame().getGamePlayers()
                .stream().map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO()).collect(Collectors.toList()));
        dto.put("ships", gamePlayerChosen.getShips()
                .stream().map(ship -> ship.makeShipDTO()).collect(Collectors.toList()));
        dto.put("salvoes", gamePlayerChosen.getSalvoes()
                .stream().map(salvo -> salvo.makeSalvoDTO()).collect(Collectors.toList()));
        dto.put("hits", hits);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    ///////////////////LOOGUING////////////////////////////////////////////////////////

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {
        /*email vacio y password vacio se pierde los datos*/
        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "Missing data"), HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email) != null) {
            return new ResponseEntity<>(makeMap("error", " Name already in use"), HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("ok", "ok"), HttpStatus.CREATED);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> playerJoinTheGame(Authentication authentication, @PathVariable long gameId) {

        Game game = gameRepository.findById(gameId).get();
        Player player1 = playerRepository.findByEmail(authentication.getName());

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No eres un player tomatela jajaja")
                    , HttpStatus.UNAUTHORIZED);
        }
        if (game == null) {
            return new ResponseEntity<>(makeMap("error", "No existe ese game"), HttpStatus.FORBIDDEN);
        }
        if (game.getGamePlayers().size() >= 2) {
            return new ResponseEntity<>(makeMap("error", "Tarde, llegaste tarde baby "), HttpStatus.FORBIDDEN);
        }

        GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(player1, game));
        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.OK);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //  / games / players / {gamePlayerId} / ship

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships",  method=RequestMethod.POST)
    public ResponseEntity<Object> playerAddShip(Authentication authentication, @PathVariable long gamePlayerId,  @RequestBody Set<Ship> ships ) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No eres un player tomatela jajaja")
                    , HttpStatus.UNAUTHORIZED);
        }

        GamePlayer gamePlayerChosen = gamePlayerRepository.findById(gamePlayerId).get();
        if ( gamePlayerChosen.getPlayer() == null) {
            return new ResponseEntity<>(makeMap("error", "Que tratas de hacer? Ese gamePlayer no existe!")
                    , HttpStatus.UNAUTHORIZED);
        }

        Player player1 = playerRepository.findByEmail(authentication.getName());

        if ( gamePlayerChosen.getPlayer().getId() != player1.getId()) {
            return new ResponseEntity<>(makeMap("error", "Quieres entrar a la cuenta de otro player? >:V tramposo!")
                    , HttpStatus.UNAUTHORIZED);
        }

//        if (gamePlayerChosen.getShips() == null) {
//            return new ResponseEntity<>(makeMap("error", "Tienes ships!! "), HttpStatus.FORBIDDEN);
//        }

        if (gamePlayerChosen.getShips().size() > 0) {
            return new ResponseEntity<>(makeMap("error", "Tarde! Ya colocaste tus naves, no puedes moverlas mas"), HttpStatus.FORBIDDEN);
        }
        gamePlayerChosen.setShips(ships);
        ships.forEach(ship -> ship.setGamePlayer(gamePlayerChosen));
        shipRepository.saveAll(ships);

        return new ResponseEntity<>(makeMap("OK", "ATR RE PIOLA"), HttpStatus.CREATED);
    }



    }