package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.AbstractPhase;
import me.acablade.bladeapi.features.AbstractFeature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Prevents block breaking
 */

public class NoBlockBreakFeature extends AbstractFeature {

    public NoBlockBreakFeature(AbstractPhase abstractPhase) {
        super(abstractPhase);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event){
        event.setCancelled(true);
    }
}
