package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String email;
    private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    ///Constructor///////////
    public Player() { }

    public Player(String email, String password) {
        this.email = email;
        this.password= password;
    }

    //GETTERS AND SETTERS
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    ///DTO
    public Map<String, Object> makePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",this.getId());
        dto.put("email", this.getEmail());
        return dto;
    }

    //SCORE public Score getScore(Game game){ return game; }

}
