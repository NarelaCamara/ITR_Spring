package com.codeoftheweb.salvo.Interfaces;

import com.codeoftheweb.salvo.ClassModel.Salvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SalvoRepository extends JpaRepository<Salvo, Long> {

}
