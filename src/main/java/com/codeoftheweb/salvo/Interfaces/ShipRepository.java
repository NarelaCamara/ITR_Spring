package com.codeoftheweb.salvo.Interfaces;

import com.codeoftheweb.salvo.ClassModel.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ShipRepository extends JpaRepository<Ship, Long> {
}
