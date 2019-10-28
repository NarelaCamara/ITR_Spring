package com.codeoftheweb.salvo.Interfaces;

        import com.codeoftheweb.salvo.ClassModel.Player;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.repository.query.Param;
        import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByEmail(@Param("email") String email);
}
