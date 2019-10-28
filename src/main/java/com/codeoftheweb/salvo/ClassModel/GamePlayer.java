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
        ship.setGamePlayer(this);
    }

    //DTO
    public Map<String, Object> makeGamePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",this.getId());
        dto.put("player",this.player.makePlayerDTO());
        dto.put("ships", this.getShips().stream().map(ship -> ship.makeShipDTO()).collect(Collectors.toList()));
        return dto;
    }

    //Salvos
    public Set<Salvo> getSalvoes (){
        return  salvos;
    }

    public void setSalvos(Set<Salvo> salvos) {
        this.salvos = salvos;
    }


}
