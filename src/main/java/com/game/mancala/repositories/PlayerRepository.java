package com.game.mancala.repositories;

import java.util.UUID;
import com.game.mancala.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the Repository class for the Player model.
 * Repository is meant for encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {
}
