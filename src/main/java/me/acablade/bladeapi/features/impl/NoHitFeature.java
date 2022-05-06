package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.AbstractPhase;
import me.acablade.bladeapi.features.AbstractFeature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Prevents players getting damaged
 */
public class NoHitFeature extends AbstractFeature {
    public NoHitFeature(AbstractPhase abstractPhase) {
        super(abstractPhase);
    }

    @EventHandler
    private void onHit(EntityDamageEvent event){
        event.setCancelled(event.getEntity() instanceof Player);
    }

}
