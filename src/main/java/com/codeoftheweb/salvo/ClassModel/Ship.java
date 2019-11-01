package com.codeoftheweb.salvo.ClassModel;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;


@Entity
public class Ship {

    //Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    ///Mi foreing key
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
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

    //DTO///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Map<String, Object> makeShipDTO() {
        Map<String, Object> dto= new HashMap<>();
        dto.put("type", this.type );
        dto.put("locations", this.getLocations());
        return dto;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/* Devuelve una lista de Strings de locaciones que le pego a ese ship */
    public List<String> devuelveLosHitsQueRecibio(List<String> locationsSalvo) {
        List<String> eseHitMePego = new ArrayList<>();
                for( String  location : locationsSalvo  ){
                    if(this.esaLocacionMePego(location)){
                        eseHitMePego.add(location);
                    }
                }
        return eseHitMePego;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*Devuelve un booleano si esa locacion le dio a ese ship*/
    public boolean esaLocacionMePego(String location1) {
        for ( String location : locations){
           if( location1 == location) {
               return true;
           }
        }
        return false;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //sacar un {}
    public Map<String, Object> devuelveDamages(Salvo salvo) {
        Map<String, Object> dto= new HashMap<>();
        dto.put(this.getType()+"Hits", (this.devuelveLosHitsQueRecibio(salvo.getLocations())).size() );
        dto.put( this.getType(), this.devuelveTodosLosHitsQueRecibio(salvo.getGamePlayer().getSalvoes()) );
        return dto;
    }



    public Integer devuelveTodosLosHitsQueRecibio(Set<Salvo> salvoes) {
        List<String> totalDeGolpes = new ArrayList<>();
        for( Salvo salvo : salvoes ) {
            totalDeGolpes.addAll(this.devuelveLosHitsQueRecibio( salvo.getLocations()));
        }
        return totalDeGolpes.size();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
