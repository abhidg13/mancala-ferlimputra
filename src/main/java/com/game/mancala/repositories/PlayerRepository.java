package com.game.mancala.repositories;

import java.util.UUID;
import com.game.mancala.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PlayerRepository
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {


}
