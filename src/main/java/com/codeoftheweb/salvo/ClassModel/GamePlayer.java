package com.codeoftheweb.salvo.ClassModel;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Entity
public class GamePlayer {
    //Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDate fechaDeCreacion;

    //foreign key Game
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    //foreign key Player
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    //foreign key Ship
    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Ship> ships ;

    //foreign key Salvo
    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Salvo> salvos ;


    //Constructor
    public GamePlayer(){ }

    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.fechaDeCreacion = LocalDate.now();
    }

    public GamePlayer(LocalDate fechaDeCreacion, Game game, Player player) {
        this.fechaDeCreacion = fechaDeCreacion;
        this.game = game;
        this.player = player;
    }

    //Getters And Setters
    //ID
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //FechaDeCreacion
    public LocalDate getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(LocalDate fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    //PLayer
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    //Game
    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    //Ships List
    public Set<Ship> getShips (){
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    //ADD SHIPS
    public void addShips(Ship ship) {
        this.ships.add(ship);
    }

    ///SALVOS
    public Set<Salvo> getSalvoes() {
        return salvos;
    }

    public void addSalvo(Salvo salvo) {
        salvos.add(salvo);
    }

    public void setSalvos(Set<Salvo> salvos) {
        this.salvos = salvos;
    }

    //DTO
    public Map<String, Object> makeGamePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",this.getId());
        dto.put("player",this.player.makePlayerDTO());
        return dto;
    }

    //Encuentra al rival del game qu esta jugando///////////////////////////////////////////////////////////////////////
    public GamePlayer findOpponent() {
        GamePlayer  gp = this.getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getId()!= this.getId()).findFirst().orElse(null);
        return gp;
    }

    ///////estado del gameplayer en el game
    public String stateGame() {
         if(this.getShips().isEmpty() )
              return "PLACESHIPS";
         if(   this.findOpponent() == null  )
            return "WAITINGFOROPP";
        if(   this.findOpponent().getShips().isEmpty()  )
            return "WAIT";
        if(   !this.findOpponent().getShips().isEmpty()  && !this.getShips().isEmpty() )
            return "PLAY";
        ////Hacerrr
//        if( this.estadoDelGame() == "WON"  )
//            return "WON";
//        if( this.findOpponent().estadoDelGame() == "WON"  )
//            return "LOST";
//        if( this.estadoDelGame() == "TIE" && this.findOpponent().estadoDelGame() == "TIE" )
//            return "TIE";
//        //ACA ESPERA EL SALVO
//        if( this.estadoDelGame() == "AGAIN")
//            return "WAIT";

//        return  "tu vieja";
//    }
//
//    private String estadoDelGame() {
//        GamePlayer rivalSalvos = (GamePlayer) this.findOpponent().getSalvoes();
//        this.getShips().forEach(ship -> { Integer total = ship.devuelveLosHitsQueRecibio(); } );

        return "TIE";
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}