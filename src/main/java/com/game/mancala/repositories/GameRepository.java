package com.game.mancala.repositories;

import java.util.UUID;
import com.game.mancala.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * GameRepository
 */
@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {


}
