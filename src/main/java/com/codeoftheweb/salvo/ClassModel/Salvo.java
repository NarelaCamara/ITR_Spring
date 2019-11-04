package com.codeoftheweb.salvo.ClassModel;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

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
    private List<String> salvoLocations;

    //Turn
    private Integer turn ;

    //Constructor///////////////////////////////////////////////////////////////////////////////////////////////////////
    public Salvo() {
    }
    public Salvo(GamePlayer gamePlayer, Integer turn,  List<String> locations) {
        this.gamePlayer = gamePlayer;
        this.turn =  turn;
        this.salvoLocations = locations;
    }

    ///GETTERS ANS SETTERS//////////////////////////////////////////////////////////////////////////////////////////////
    public Integer getTurn(Integer turn) {
        return this.turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
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

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Map<String, Object> makeSalvoDTO() {
        Map<String, Object> dto= new HashMap<>();
       dto.put("player", gamePlayer.getPlayer().getId());
        dto.put("turn", this.getTurn(turn) );
        dto.put("locations", this.getSalvoLocations());
        return dto;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Map<String, Object> makeTurn(Set<Ship> ships) {
        Map<String, Object> dto= new LinkedHashMap<>();
        dto.put("turn", this.getTurn(turn));
        dto.put("hitLocations", this.locationsHits(ships) );
        dto.put("damages", this.getDamages(ships));
        dto.put("missed", this.locationsMissed(ships) );
        return dto;
}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   public   Map<String, Object> getDamages(Set<Ship> ships) {
        Map<String, Object> dto= new LinkedHashMap<>();
        ships.forEach(ship -> dto.put(ship.getType(), ship.devuelveLosHitsQueRecibio(this.getSalvoLocations()).size() ));
        ships.forEach(ship -> dto.put(ship.getType()+"Hits",  ship.devuelveTodosLosHitsQueRecibio(this.getGamePlayer().getSalvoes())));
        return dto;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*Los salvos que dieron en el blanco*/
    public List<String> locationsHits(Set<Ship> ships) {
        List<String> todoLosHitsQuePegaron = new ArrayList<>();
        for ( String locacion : salvoLocations){
            if(   ships.stream().anyMatch(ship -> ship.esaLocacionMePego(locacion) ) ){
                todoLosHitsQuePegaron.add(locacion);
            }
        }
        return todoLosHitsQuePegaron;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*Cantidad de Locaciones que no dieron en el blanco*/
    public Integer locationsMissed( Set<Ship> ships) {
     return this.getSalvoLocations().size() - this.locationsHits( ships).size();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



}
