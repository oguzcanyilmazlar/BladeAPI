package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.AbstractPhase;
import me.acablade.bladeapi.features.AbstractFeature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Acablade/oz
 */

public class AutoRespawnOnDeathFeature extends AbstractFeature {
    public AutoRespawnOnDeathFeature(AbstractPhase abstractPhase) {
        super(abstractPhase);
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        (new BukkitRunnable(){
            @Override
            public void run() {
                event.getEntity().spigot().respawn();
            }
        }).runTaskLater(getAbstractPhase().getGame().getPlugin(),1L);
    }


}
