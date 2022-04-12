package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.AbstractPhase;
import me.acablade.bladeapi.features.AbstractFeature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Acablade/oz
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
            onDisable();
            return;
        }

        mapFeature = (MapFeature) getAbstractPhase().getFeature(MapFeature.class);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();

        player.teleport(mapFeature.getSpawnPoints().get(getAbstractPhase().getGame().getGameData().getPlayerList().size()));

    }
}
