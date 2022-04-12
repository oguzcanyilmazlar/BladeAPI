package me.acablade.bladeapi.features;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.acablade.bladeapi.AbstractPhase;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * @author Acablade/oz
 */

@RequiredArgsConstructor
@Getter
public abstract class AbstractFeature implements Listener {

    private final AbstractPhase abstractPhase;

    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(this,abstractPhase.getGame().getPlugin());
    }
    public void onDisable(){
        HandlerList.unregisterAll(this);
    }
    public void onTick(){}

}
