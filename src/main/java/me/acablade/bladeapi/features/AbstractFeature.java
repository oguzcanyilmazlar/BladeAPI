package me.acablade.bladeapi.features;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.acablade.bladeapi.AbstractPhase;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
@Getter
public abstract class AbstractFeature implements Listener, IFeature {


    private final AbstractPhase abstractPhase;
    private final JavaPlugin plugin;


    protected void runLater(Runnable runnable, long delay){
        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public final void enable(){
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.onEnable();
    }

    public final void disable(){
        HandlerList.unregisterAll(this);
        this.onDisable();
    }

}
