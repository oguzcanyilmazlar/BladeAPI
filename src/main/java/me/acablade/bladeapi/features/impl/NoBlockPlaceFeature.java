package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.AbstractPhase;
import me.acablade.bladeapi.features.AbstractFeature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Prevents block placing
 */
public class NoBlockPlaceFeature extends AbstractFeature {

    public NoBlockPlaceFeature(AbstractPhase abstractPhase) {
        super(abstractPhase);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event){
        event.setCancelled(true);
    }

}
