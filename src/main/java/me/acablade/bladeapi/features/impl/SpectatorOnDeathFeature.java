package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.AbstractPhase;
import me.acablade.bladeapi.features.AbstractFeature;
import me.acablade.bladeapi.objects.GameData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

/**
 * Makes player spectator on death
 */
public class SpectatorOnDeathFeature extends AbstractFeature {

    public SpectatorOnDeathFeature(AbstractPhase abstractPhase) {
        super(abstractPhase);
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();

        GameData gameData = getAbstractPhase().getGame().getGameData();

        gameData.getSpectatorList().add(uuid);
        gameData.getPlayerList().remove(uuid);

        player.setGameMode(GameMode.SPECTATOR);
    }

}
