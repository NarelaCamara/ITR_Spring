package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.ClassModel.*;
import com.codeoftheweb.salvo.Interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;



@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(GameRepository repositoryGame, PlayerRepository repositoryPlayer, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
        return (args) -> {
//            Player ro = new Player("roxana.camara07@gmail.com", passwordEncoder().encode("22"));
//            Player damo = new Player("damian.martilotta@gmail.com",  passwordEncoder().encode("25"));
//            Player tami = new Player("tamara.benitez@gmail.com",  passwordEncoder().encode("22"));
//
//            Player dani = new Player("melina@gmail.com",  passwordEncoder().encode("27"));
//            Player mely = new Player("daniela@gmail.com",  passwordEncoder().encode("25"));
//
//
//            repositoryPlayer.save(dani);
//            repositoryPlayer.save(mely);
//
//            repositoryPlayer.save(ro);
//            repositoryPlayer.save(damo);
//            repositoryPlayer.save(tami);
//
//            Game halfLife = new Game();
//            Game fortNite = new Game(LocalDateTime.now().plusHours(5));
//
//            repositoryGame.save(halfLife);
//            repositoryGame.save(fortNite);
//
//
//            GamePlayer gamePlayer1 = new GamePlayer(ro, halfLife);
//            GamePlayer gamePlayer2 = new GamePlayer(damo, halfLife);
//            GamePlayer gamePlayer3 = new GamePlayer(tami, fortNite);
//            GamePlayer gamePlayer4 = new GamePlayer(ro, fortNite);
//
//            gamePlayerRepository.save(gamePlayer1);
//            gamePlayerRepository.save(gamePlayer2);
//            gamePlayerRepository.save(gamePlayer3);
//            gamePlayerRepository.save(gamePlayer4);
//
//
//            List<String> localizacion1 = new ArrayList<>();
//            localizacion1.addAll(Arrays.asList("H2", "H3", "H4"));
//
//            List<String> localizacion2 = new ArrayList<>();
//            localizacion2.addAll(Arrays.asList("F2", "F3", "F4"));
//
//            List<String> localizacion3 = new ArrayList<>();
//            localizacion3.addAll(Arrays.asList("C2", "C3", "C4"));
//
//            Ship titanic = new Ship("crucero", localizacion1, gamePlayer1);
//            Ship potemkin = new Ship("destructor", localizacion2, gamePlayer1);
//
//            Ship santaRosa = new Ship("destructor", localizacion3, gamePlayer2);
//            Ship santaMarta = new Ship("urss", localizacion3, gamePlayer2);
//
//
//            Ship santaMaria = new Ship("china", localizacion3, gamePlayer3);
//            Ship queenMary = new Ship("acorazado", localizacion3, gamePlayer4);
//
//            shipRepository.save(santaRosa);
//            shipRepository.save(santaMarta);
//            shipRepository.save(titanic);
//            shipRepository.save(potemkin);
//            shipRepository.save(santaMaria);
//            shipRepository.save(queenMary);
//
//
//            List<String> localizacion4 = new ArrayList<>();
//            localizacion4.addAll(Arrays.asList("D2", "D3", "D4"));
//
//            List<String> localizacion5 = new ArrayList<>();
//            localizacion5.addAll(Arrays.asList("F5", "F6", "F7"));
//
//            List<String> localizacion6 = new ArrayList<>();
//            localizacion6.addAll(Arrays.asList("A2", "A3", "A4"));
//
//
//
////Salvos Ro
//            Salvo shoot1A = new Salvo(gamePlayer1,1, localizacion5);
//            Salvo shoot1B = new Salvo(gamePlayer1,2,  localizacion3);
////Salvos Damo
//            Salvo shoot2A = new Salvo(gamePlayer2, 1, localizacion1);
//            Salvo shoot2B = new Salvo(gamePlayer2,2, localizacion6);
//
////            Salvo shoot4 = new Salvo(gamePlayer4,  localizacion4);
//
//
//            salvoRepository.save(shoot1A);
//            salvoRepository.save(shoot1B);
//
//
//            salvoRepository.save(shoot2A);
//            salvoRepository.save(shoot2B);
////            salvoRepository.save(shoot4);
//
//
//            LocalDateTime fecha1 = LocalDateTime.now().plusHours(14);
//            float ganador1 = (float) 1.0;
//
//            LocalDateTime fecha2 = LocalDateTime.now().plusHours(16);
//            float ganador2 = (float) 0.5;
//
//            Score ganadorRo = new Score(halfLife, ro, ganador1, fecha1);
//            Score ganadorDamo = new Score(halfLife, damo, ganador2, fecha2);
//
//            scoreRepository.save(ganadorRo);
//            scoreRepository.save(ganadorDamo);
        };
    }
}

/*decirle a Spring que use esta base de datos para la autenticación
* gregamos la siguiente clase de WebSecurityConfiguration */



    @Configuration
    @EnableWebSecurity
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/web/**").permitAll()
                .antMatchers("/api/games").permitAll()
                .antMatchers("/api/players").permitAll()
                .antMatchers("/api/game_view").hasAuthority("USER")
                .antMatchers("*/h2-console/**").permitAll()
                .antMatchers("/api/login/").permitAll()
                .and().headers().frameOptions().sameOrigin();//allow use of frame to same origin urls
        http.formLogin()
                .usernameParameter("name")
                .passwordParameter("pwd")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");


        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }


    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

////////////////////////////////////////////////////////////////
@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository;
    /*Aca se haria EL INICIO DE SESION*/
    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        /*El trabajo de esta nueva clase es tomar el nombre que alguien ha ingresado para iniciar sesión,
         buscar en la base de datos con ese nombre y devolver un  objeto
         UserDetails con nombre, contraseña e información de rol para ese usuario, si corresponde.*/
        auth.userDetailsService(inputName-> {
            Player person = playerRepository.findByEmail(inputName);
            if (person != null) {
                return new User(person.getEmail(), person.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}