package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.ClassModel.*;
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

import static java.util.stream.Collectors.toList;

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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    /////////////////////////////METODO PARA HACER UN USUARIO INCOGNITO/NO REGISTRADO //////////////////////////////////
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("/games")
    public Map<String, Object> player(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (isGuest(authentication)) {
            dto.put("player", "Guest");
        } else {
            Player player = playerRepository.findByEmail(authentication.getName());
            dto.put("player", player.makePlayerDTO());
        }
        dto.put("games", gameRepository.findAll().stream().map(game -> game.makeGameDTO()).collect(toList()));
        return dto;
    }

    ///////////////////LOOGUING/////////////////////////////////////////////////////////////////////////////////////////

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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(path = "/games/players/{gamePlayerId}/ships",  method=RequestMethod.POST)
    public ResponseEntity<Object> playerAddShip(Authentication authentication, @PathVariable long gamePlayerId,  @RequestBody Set<Ship> ships ) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No eres un player tomatela jajaja")
                    , HttpStatus.UNAUTHORIZED);
        }

        GamePlayer gamePlayerChosen = gamePlayerRepository.findById(gamePlayerId).get();

        if ( gamePlayerChosen == null) {
            return new ResponseEntity<>(makeMap("error", "Que tratas de hacer? Ese gamePlayer no existe!")
                    , HttpStatus.UNAUTHORIZED);
        }

        Player player1 = playerRepository.findByEmail(authentication.getName());

        if ( gamePlayerChosen.getPlayer().getId() != player1.getId()) {
            return new ResponseEntity<>(makeMap("error", "Quieres entrar a la cuenta de otro player? >:V tramposo!")
                    , HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayerChosen.getShips().size() > 0) {
            return new ResponseEntity<>(makeMap("error", "Tarde! Ya colocaste tus naves, no puedes moverlas mas"), HttpStatus.FORBIDDEN);
        }


        ships.forEach(ship -> ship.setGamePlayer(gamePlayerChosen));
        shipRepository.saveAll( ships);

        return new ResponseEntity<>(makeMap("OK", "ATR RE PIOLA"), HttpStatus.CREATED);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvos",  method=RequestMethod.POST)
    public ResponseEntity<Object> playerAddSalvo(Authentication authentication, @PathVariable long gamePlayerId,  @RequestBody Salvo salvo ) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No eres un player tomatela jajaja")
                    , HttpStatus.UNAUTHORIZED);
        }

        GamePlayer gamePlayerChosen = gamePlayerRepository.findById(gamePlayerId).get();

        if (gamePlayerChosen.getPlayer() == null) {
            return new ResponseEntity<>(makeMap("error", "Que tratas de hacer? Ese gamePlayer no existe!")
                    , HttpStatus.UNAUTHORIZED);
        }

        Player player1 = playerRepository.findByEmail(authentication.getName());
        if (gamePlayerChosen.getPlayer().getId() != player1.getId()) {
            return new ResponseEntity<>(makeMap("error", "Quieres entrar a los salvos de otro player? >:V tramposo!")
                    , HttpStatus.UNAUTHORIZED);
        }
//        Integer turno = gamePlayerChosen.getSalvoes().size() +1;
//        Salvo salvoNew = new Salvo(gamePlayerChosen, turno, salvo.getLocations() );
    Boolean seRepite = gamePlayerChosen.getSalvoes().stream().anyMatch(salvo1 -> salvo1.getTurno() ==  gamePlayerChosen.getSalvoes().size() );

    if( seRepite ){
       return new ResponseEntity<>(makeMap("error", "Ese turno estaba rancio! ya paso!"), HttpStatus.FORBIDDEN);
    }

    if(  gamePlayerId != gamePlayerChosen.getId()  ){
         return new ResponseEntity<>(makeMap("error", " ¿¿WTF??"), HttpStatus.FORBIDDEN);
    }

//    salvoRepository.save(salvoNew);
    return new ResponseEntity<>(makeMap("OK", "ATR RE PIOLA LOS SALVOS"), HttpStatus.CREATED);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @RequestMapping(path ="/game_view/{id_gp}")
    public ResponseEntity<Object> gamePlayer1(Authentication authentication, @PathVariable Long id_gp) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No eres un player tomatela jajaja")
                    , HttpStatus.UNAUTHORIZED);
        }


        Player player = playerRepository.findByEmail( authentication.getName() );

        GamePlayer gamePlayerChosen = gamePlayerRepository.findById(id_gp).orElse(null);

        if (player == null && gamePlayerChosen == null) {
            return new ResponseEntity<>(makeMap("error", "Problemas o eres un player o gameplayer inexistente"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayerChosen.getPlayer().getId() != player.getId()) {
            return new ResponseEntity<>(makeMap("error", "No seas tramposo, eres un player pero no el que dices e.e")
                    , HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", gamePlayerChosen.getGame().getId());
        dto.put("created", gamePlayerChosen.getGame().getFechaDeCreacion());

        //////////////ACA ESTOY///////////
        dto.put("gameState", gamePlayerChosen.stateGame());


        dto.put("gamePlayers", gamePlayerChosen.getGame().getGamePlayers()
                .stream().map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO()).collect(toList()));
        dto.put("ships", gamePlayerChosen.getShips()
                .stream().map(ship -> ship.makeShipDTO()).collect(toList()));
        dto.put("salvoes", gamePlayerChosen.getSalvoes()
                .stream().map(salvo -> salvo.makeSalvoDTO()).collect(toList()));
        dto.put("hits",  makeHits( gamePlayerChosen));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Map<String, Object>makeHits(GamePlayer gamePlayerChosen) {
        Map<String, Object> dto = new LinkedHashMap<>();
            GamePlayer rival = gamePlayerChosen.findOpponent();
            List <Map<String, Object>> self = new ArrayList<Map<String, Object>>();

            /*EL DTO VACIO su razon de estar es porque el front  pide leer una lista con self y opponent */
            if (rival == null ){
                dto.put("self", self);
                dto.put("opponent",self);
            }else{
            dto.put("self", rival.getSalvoes().stream().map(salvo -> salvo.makeTurn(gamePlayerChosen.getShips())));
            dto.put("opponent", gamePlayerChosen.getSalvoes().stream().map(salvo -> salvo.makeTurn(rival.getShips())));
        }
        return dto;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



