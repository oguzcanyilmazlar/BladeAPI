package me.acablade.bladeapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.acablade.bladeapi.events.GamePhaseChangeEvent;
import me.acablade.bladeapi.objects.GameData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * @author Acablade/oz
 */

@RequiredArgsConstructor
public abstract class AbstractGame {

    @Getter
    private final String name;
    private final JavaPlugin plugin;

    private int taskNumber = -1;

    private int currentPhaseIndex = 0;
    private AbstractPhase currentPhase;

    private boolean frozen;

    private final LinkedList<Class<? extends AbstractPhase>> phaseLinkedList = new LinkedList<>();
    private GameData gameData = new GameData();

    /**
     *
     */
    public void onEnable(){}

    public void endPhase(){
        if(isFrozen()) return;
        try {
            AbstractPhase phase = phaseLinkedList.get(currentPhaseIndex).getDeclaredConstructor(AbstractGame.class).newInstance(this);
            GamePhaseChangeEvent phaseChangeEvent = new GamePhaseChangeEvent(this, this.currentPhase,phase);
            Bukkit.getPluginManager().callEvent(phaseChangeEvent);
            if(phaseChangeEvent.isCancelled()) return;
            if(this.currentPhase!=null)this.currentPhase.disable();
            if(currentPhaseIndex>=phaseLinkedList.size()) {
                disable();
                return;
            }
            phase.enable();
            this.currentPhase = phase;
            this.currentPhaseIndex++;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            getPlugin().getLogger().warning("Error occurred when ending the current phase: " + e.getMessage());
        }
    }

    public final void enable(long delay, long tick){
        if(taskNumber>0) return;
        onEnable();
        endPhase();
        taskNumber = Bukkit.getScheduler().runTaskTimer(plugin,this::tick,delay,tick).getTaskId();
    }

    public void addPhaseNext(Class<? extends AbstractPhase> clazz){
        this.phaseLinkedList.add(currentPhaseIndex+1, clazz);
    }

    public void removeNextPhase(){
        this.phaseLinkedList.remove(currentPhaseIndex+1);
    }

    public void addPhase(Class<? extends AbstractPhase> clazz){
        this.phaseLinkedList.addLast(clazz);
    }

    public void removeLastPhase(){
        this.phaseLinkedList.removeLast();
    }

    public void onDisable(){
        if(this.currentPhase!=null)currentPhase.disable();
    }

    public final void disable(){
        onDisable();
        Bukkit.getScheduler().cancelTask(taskNumber);
    }

    public void tick(){
        if(getCurrentPhase()!=null)getCurrentPhase().tick();
        if(!frozen&&getCurrentPhase().timeLeft().isZero()) endPhase();
    }

    public AbstractPhase getCurrentPhase() {
        return currentPhase;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }
}
