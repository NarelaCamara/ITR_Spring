package com.codeoftheweb.salvo.ClassModel;

import com.codeoftheweb.salvo.ClassModel.Game;
import com.codeoftheweb.salvo.ClassModel.Player;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    //Foreng Key Game
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    //Foreng Key Player
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    ///PUEDE CAMBIAR
    private float score;
    private LocalDateTime finishDate;

    //Constructor
    public Score(){}

    public Score(Game game, Player player, float score, LocalDateTime finishDate ){
        this.game = game;
        this.player = player;
        this.score = score;
        this.finishDate = finishDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    //DTO
    public Map<String, Object> makeScoreDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player", player.getId());
        dto.put("score", this.getScore());
        dto.put("finishDate", this.getFinishDate());
        return dto;
    }


}
