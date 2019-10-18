package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;


@Entity
public class GamePlayer {
    //Primary bKey
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDate fechaDeCreacion;

    //Foreng Key Game
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    //Foreng Key Player
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    //Foreng Key Ship
    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    List<Ship> ships ;

    //Constructor
    public GamePlayer(){ }

    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.fechaDeCreacion = LocalDate.now();
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
    public List<Ship> getShips (){
        return ships;
    }

    //ADD SHIPS
    public void addShips(Ship ship) {
        this.ships.add(ship);
        ship.setGamePlayer(this);
    }
    //DTO
    public Map<String, Object> makeGamePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",this.id);
        dto.put("player",this.player.makePlayerDTO());
        return dto;
    }



}
