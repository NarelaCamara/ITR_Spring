package com.codeoftheweb.salvo.Interfaces;

import com.codeoftheweb.salvo.ClassModel.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {

    // GamePlayer findByEmail(@Param("email") String email) ;
}

