package com.codeoftheweb.salvo.ClassModel;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

import java.time.LocalDateTime;

import java.util.*;

import java.util.stream.Collectors;


@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime fechaDeCreacion;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private List<Score> scores;

    //Constructor
    public Game() {
        this.fechaDeCreacion =  LocalDateTime.now();
    }

    public Game(LocalDateTime fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion ;
    }

    //Getters AND Setters
    public LocalDateTime getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(LocalDateTime fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(Score scores) {
        this.scores.add( scores );
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    //DTO
   public Map<String, Object> makeGameDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("created", this.getFechaDeCreacion());
        dto.put("gamePlayers", this.getGamePlayers().stream().map(gamePlayer -> gamePlayer.makeGamePlayerDTO()).collect(Collectors.toList()));
        dto.put("scores", this.getScores().stream().map(score -> score.makeScoreDTO())) ;
        return dto;
   }

    //SCORE

}


