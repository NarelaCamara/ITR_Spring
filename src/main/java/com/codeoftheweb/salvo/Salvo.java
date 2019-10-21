package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    ///Mi foreing key
    @ManyToOne
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    private List<String> locations;

    //Turno
    private Integer turno;

    //Constructor
    public Salvo() {
    }

    public Salvo(GamePlayer gamePlayer, Integer turno, List<String> locations) {
        this.gamePlayer = gamePlayer;
        this.turno = turno;
        this.locations = locations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public Integer getTurno() {
        return turno;
    }

    public void setTurno(Integer turno) {
        this.turno = turno;
    }

    public Map<String, Object> makeSalvoDTO() {
        Map<String, Object> dto= new HashMap<>();
        dto.put("player", gamePlayer.getPlayer().getId());
        dto.put("turn", this.turno );
        dto.put("locations", this.getLocations());
        return dto;
    }
}
