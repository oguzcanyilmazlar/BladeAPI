package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.AbstractPhase;
import me.acablade.bladeapi.events.PlayerJoinGameEvent;
import me.acablade.bladeapi.features.AbstractFeature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Spawns players automatically on spawn locations set in MapFeature
 */


public class SpawnOnSpawnLocationFeature extends AbstractFeature {

    private MapFeature mapFeature;

    public SpawnOnSpawnLocationFeature(AbstractPhase abstractPhase) {
        super(abstractPhase);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if(getAbstractPhase().getFeature(MapFeature.class)==null){
            disable();
            return;
        }

        mapFeature = getAbstractPhase().getFeature(MapFeature.class);

    }

    @EventHandler
    private void onJoin(PlayerJoinGameEvent event){

        Player player = event.getPlayer();

        player.teleport(mapFeature.getSpawnPoints().get(getAbstractPhase().getGame().getGameData().getPlayerList().size()));

    }
}
