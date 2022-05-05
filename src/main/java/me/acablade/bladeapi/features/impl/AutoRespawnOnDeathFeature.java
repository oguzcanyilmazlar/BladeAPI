package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.AbstractPhase;
import me.acablade.bladeapi.features.AbstractFeature;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author Acablade/oz
 */

public class AutoRespawnOnDeathFeature extends AbstractFeature {
    public AutoRespawnOnDeathFeature(AbstractPhase abstractPhase) {
        super(abstractPhase);
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Bukkit.getScheduler().runTaskLater(getAbstractPhase().getGame().getPlugin(), () -> event.getEntity().spigot().respawn(),1L);
    }


}
