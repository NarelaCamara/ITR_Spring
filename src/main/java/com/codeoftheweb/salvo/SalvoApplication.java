package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.time.LocalDateTime;
import java.util.*;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(GameRepository repositoryGame, PlayerRepository repositoryPlayer, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository) {
		return (args) -> {
			Player ro = new Player("Roxana.Camara07@gmail.com");
			Player damo = new Player("Damian.Martilotta@gmail.com");
			Player tami = new Player("TamaraBrian@gmail.com");

			repositoryPlayer.save(ro);
			repositoryPlayer.save(damo);
			repositoryPlayer.save(tami);

			 Game halfLife = new Game();
			repositoryGame.save( halfLife );
			Game fortNite = new Game(  LocalDateTime.now().plusHours( 5));
			repositoryGame.save( fortNite );


			GamePlayer gamePlayer1= new GamePlayer( ro, halfLife );
            gamePlayerRepository.save(gamePlayer1);
			GamePlayer gamePlayer2= new GamePlayer( damo, halfLife);
            gamePlayerRepository.save(gamePlayer2);
			GamePlayer gamePlayer3= new GamePlayer( damo, fortNite);
			gamePlayerRepository.save(gamePlayer3);


			List<String> localizacion1 = new ArrayList<>();
			localizacion1.addAll(Arrays.asList("H2", "H3", "H4"));

            List<String> localizacion2 = new ArrayList<>();
            localizacion2.addAll(Arrays.asList("F2", "F3", "F4"));

            List<String> localizacion3 = new ArrayList<>();
            localizacion3.addAll(Arrays.asList("C2", "C3", "C4"));

			Ship titanic = new Ship("crucero", localizacion1, gamePlayer1);
			Ship potemkin = new Ship("destructor", localizacion2, gamePlayer1);
			Ship santaMaria = new Ship("acorazado", localizacion3, gamePlayer2);

			//gamePlayer1.addShips(titanic);
			//gamePlayer2.addShips(santaMaria);
			// gamePlayer3.addShips(santaMaria);

			shipRepository.save( titanic);
			shipRepository.save( potemkin);
			shipRepository.save( santaMaria);


		};
	}

}