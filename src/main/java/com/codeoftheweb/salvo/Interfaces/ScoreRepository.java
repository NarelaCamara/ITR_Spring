package com.codeoftheweb.salvo.Interfaces;

import com.codeoftheweb.salvo.ClassModel.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
