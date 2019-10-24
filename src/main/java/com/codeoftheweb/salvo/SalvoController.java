package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.LinkedHashMap;

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

    @Autowired
    private PasswordEncoder passwordEncoder;


    //////////////////////////////////////////
    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getPlayerAndShipsAndSalvoes(@PathVariable long nn) {
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


    ///////////////////LOOGUING////////////////////////////////////////////////////////
 /* necesita verificar que los datos sean válidos, por ejemplo,
    sin cadenas están vacías, la contraseña es lo suficientemente complicada
    y la dirección de correo electrónico aún no está en uso. Si alguna de esas pruebas falla,
    el método debería responder con un error. De lo contrario, debería crear y guardar una nueva  player*/

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String email, @RequestParam String password) {
/*email vacio y password vacio se pierde los datos*/
        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

/////////////////////////////METODO PARA HACER UN USUARIO INCOGNITO/NO REGISTRADO //////////////////////////////////////
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    ///////////////////////////////////////////////////////////////
    @RequestMapping("/games")
    public Map<String, Object> player(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (isGuest(authentication)) {
            dto.put("player", "Guest");
        }else
        {
            Player player = playerRepository.findByEmail(authentication.getName());
            dto.put("player", player.makePlayerDTO() );
        }
        dto.put("games", gameRepository.findAll().stream().map(game -> game.makeGameDTO()).collect(Collectors.toList()));
                return dto;
    }


}


