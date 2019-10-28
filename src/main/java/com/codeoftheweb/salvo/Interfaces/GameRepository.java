package com.codeoftheweb.salvo.Interfaces;

import com.codeoftheweb.salvo.ClassModel.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {
}
