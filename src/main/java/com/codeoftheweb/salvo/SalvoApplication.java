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
	public CommandLineRunner initData(GameRepository repositoryGame, PlayerRepository repositoryPlayer, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
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


			List<String> localizacion4 = new ArrayList<>();
			localizacion4.addAll(Arrays.asList("D2", "D3", "D4"));

			List<String> localizacion5 = new ArrayList<>();
			localizacion5.addAll(Arrays.asList("F5", "F6", "F7"));

			List<String> localizacion6 = new ArrayList<>();
			localizacion6.addAll(Arrays.asList("A2", "A3", "A4"));

		Salvo shoot1 = new Salvo(gamePlayer1, 1, localizacion4 );
		Salvo shoot2 = new Salvo(gamePlayer2, 1, localizacion5 );
		Salvo shoot3 = new Salvo(gamePlayer1, 2, localizacion6 );

		salvoRepository.save(shoot1);
		salvoRepository.save(shoot2);
		salvoRepository.save(shoot3);

		LocalDateTime fecha1 =  LocalDateTime.now().plusHours( 14);
		float ganador1 = (float) 1.0;

		LocalDateTime fecha2 =  LocalDateTime.now().plusHours( 16);
		float ganador2 = (float) 0.5;

		Score ganadorRo = new Score( halfLife, ro, ganador1, fecha1 );
		Score ganadorDamo = new Score( halfLife, damo, ganador2, fecha2 );

		scoreRepository.save(ganadorRo);
		scoreRepository.save(ganadorDamo);


		};
	}

}