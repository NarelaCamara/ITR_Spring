package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
public class Ship {

    //Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    ///Mi foreing key
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    private List<String> locations;

    //Tipo del Ship
     private String type; //un crucero, destructor o acorazado

    //Constructores
    public Ship() {
    }

    public Ship(String type, List locations, GamePlayer gamePlayer) {
        this.type = type;
        this.locations = locations;
       this.gamePlayer = gamePlayer;
    }

    //Getters and Setters
    //ID
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //Type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //GamePlayer
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    //Locations
    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    //DTO
    public Map<String, Object> makeShipDTO() {
        Map<String, Object> dto= new HashMap<>();
        dto.put("type", this.type );
        dto.put("locations", this.locations);
        return dto;
    }


}
