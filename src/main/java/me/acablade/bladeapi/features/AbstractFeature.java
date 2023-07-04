package me.acablade.bladeapi.features;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.acablade.bladeapi.AbstractGame;
import me.acablade.bladeapi.AbstractPhase;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Acablade/oz
 */

@RequiredArgsConstructor
@Getter
public abstract class AbstractFeature<T extends AbstractGame> implements Listener {

    /**
     * Phase the feature is in.
     */
    private final AbstractPhase<T> abstractPhase;

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


    protected void runLater(Runnable runnable, long delay){
        JavaPlugin plugin = this.abstractPhase.getGame().getPlugin();
        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

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
