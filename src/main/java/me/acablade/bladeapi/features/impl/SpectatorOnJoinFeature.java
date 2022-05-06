package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.AbstractPhase;
import me.acablade.bladeapi.events.PlayerJoinGameEvent;
import me.acablade.bladeapi.features.AbstractFeature;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 * Makes player spectator on join
 */

public class SpectatorOnJoinFeature extends AbstractFeature {


    public SpectatorOnJoinFeature(AbstractPhase abstractPhase) {
        super(abstractPhase);
    }

    @EventHandler
    private void onJoin(PlayerJoinGameEvent event){
        Player player = event.getPlayer();
        getAbstractPhase().getGame().getGameData().getSpectatorList().add(player.getUniqueId());
        player.setGameMode(GameMode.SPECTATOR);
    }
}
