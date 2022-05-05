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

    /**
     * Gets called when the feature gets enabled
     */
    public void onEnable(){}

    /**
     * Gets called when the feature gets disabled
     */
    public void onDisable(){}

    /**
     * Gets called every tick
     */
    public void onTick(){}

    /**
     * Enables the feature
     */
    public final void enable(){
        Bukkit.getPluginManager().registerEvents(this,abstractPhase.getGame().getPlugin());
        this.onEnable();
    }

    /**
     * Disables the feature
     */
    public final void disable(){
        HandlerList.unregisterAll(this);
        this.onDisable();
    }

}
