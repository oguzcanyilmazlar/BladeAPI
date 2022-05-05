package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.AbstractPhase;
import me.acablade.bladeapi.features.AbstractFeature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * @author Acablade/oz
 */
public class NoBlockPlaceFeature extends AbstractFeature {

    public NoBlockPlaceFeature(AbstractPhase abstractPhase) {
        super(abstractPhase);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        event.setCancelled(true);
    }

}
