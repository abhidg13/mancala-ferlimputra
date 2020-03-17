package com.game.mancala.repositories;

import java.util.UUID;
import com.game.mancala.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the Repository class for the Game model.
 * Repository is meant for encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
}
