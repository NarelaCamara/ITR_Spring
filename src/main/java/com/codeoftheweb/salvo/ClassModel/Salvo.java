package com.codeoftheweb.salvo.ClassModel;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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
    private Integer turn ;

    //Constructor
    public Salvo() {
    }
    public Salvo(GamePlayer gamePlayer, Integer turn,  List<String> locations) {
        this.gamePlayer = gamePlayer;
        this.turn =  turn;
        this.locations = locations;
    }

//    public Salvo(GamePlayer gamePlayer,  List<String> locations) {
//        this.gamePlayer = gamePlayer;
//        this.turn =  gamePlayer.getSalvoes().size();
//        this.locations = locations;
//    }


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
        return turn;
    }

    public void setTurno(Integer turno) {
        this.turn = turno;
    }

    public Map<String, Object> makeSalvoDTO() {
        Map<String, Object> dto= new HashMap<>();
       dto.put("player", gamePlayer.getPlayer().getId());
        dto.put("turn", this.getTurno() );
        dto.put("salvoLocations", this.getLocations());
        return dto;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Map<String, Object> makeTurn(Set<Ship> ships) {
        Map<String, Object> dto= new HashMap<>();
        dto.put("turn", this.getTurno());
        dto.put("hitLocations", this.locationsHits(ships) );
        dto.put("damages", this.getDamages(ships));
        dto.put("missed", this.locationsMissed(ships) );
        return dto;
}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   public   Map<String, Object> getDamages(Set<Ship> ships) {
        Map<String, Object> dto= new LinkedHashMap<>();
        ships.forEach(ship -> dto.put(ship.getType(), ship.devuelveLosHitsQueRecibio(this.getLocations()).size() ));
        ships.forEach(ship -> dto.put(ship.getType()+"Hits",  ship.devuelveTodosLosHitsQueRecibio(this.getGamePlayer().getSalvoes())));
        return dto;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*Los salvos que dieron en el blanco*/
    public List<String> locationsHits(Set<Ship> ships) {
        List<String> todoLosHitsQuePegaron = new ArrayList<>();
        for ( String locacion : locations  ){
            if(   ships.stream().anyMatch(ship -> ship.esaLocacionMePego(locacion) ) ){
                todoLosHitsQuePegaron.add(locacion);
            }
        }
        return todoLosHitsQuePegaron;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*Cantidad de Locaciones que no dieron en el blanco*/
    public Integer locationsMissed( Set<Ship> ships) {
     return this.getLocations().size() - this.locationsHits( ships).size();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



}
