package me.acablade.bladeapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.acablade.bladeapi.events.GameFinishEvent;
import me.acablade.bladeapi.events.GamePhaseChangeEvent;
import me.acablade.bladeapi.events.GameStartEvent;
import me.acablade.bladeapi.objects.GameData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * @author Acablade/oz
 */

@RequiredArgsConstructor
public abstract class AbstractGame {

    /**
     * Game name
     */
    @Getter
    private final String name;

    /**
     * Main plugin
     */
    private final JavaPlugin plugin;

    /**
     * Task number so the game doesnt get enabled more than once
     */
    private int taskNumber = -1;

    /**
     * Self explanatory
     */
    private int currentPhaseIndex = 0;

    /**
     * Current phase
     */
    private AbstractPhase currentPhase;

    /**
     * Game frozen state
     */
    private boolean frozen;

    /**
     * Phase list
     */
    private final LinkedList<Class<? extends AbstractPhase>> phaseLinkedList = new LinkedList<>();

    /**
     * Game data
     */
    private GameData gameData = new GameData();

    /**
     * Gets called when the game starts
     */
    public void onEnable(){}

    /**
     * Ends the current game phase
     */
    public void endPhase(){
        if(isFrozen()) return;
        try {
            if(currentPhaseIndex>=phaseLinkedList.size()) {
                disable();
                return;
            }
            Constructor<?> constructor = phaseLinkedList.get(currentPhaseIndex).getDeclaredConstructors()[0];
            if(!AbstractGame.class.isAssignableFrom(constructor.getParameterTypes()[0])){
                return;
            }
            AbstractPhase phase = (AbstractPhase) constructor.newInstance(this);
            GamePhaseChangeEvent phaseChangeEvent = new GamePhaseChangeEvent(this, this.currentPhase,phase);
            Bukkit.getPluginManager().callEvent(phaseChangeEvent);
            if(phaseChangeEvent.isCancelled()) return;
            if(this.currentPhase!=null)this.currentPhase.disable();
            phase.enable();
            this.currentPhase = phase;
            this.currentPhaseIndex++;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the game
     * @param delay Runnable delay
     * @param period Runnable period
     */
    public final void enable(long delay, long period){
        if(taskNumber>0) return;
        plugin.getServer().getPluginManager().callEvent(new GameStartEvent(this));
        onEnable();
        endPhase();
        taskNumber = Bukkit.getScheduler().runTaskTimer(plugin,this::tick,delay,period).getTaskId();
    }

    /**
     * Adds a phase directly after the current phase
     * @param clazz Phase you want to add
     */
    public void addPhaseNext(Class<? extends AbstractPhase> clazz){
        this.phaseLinkedList.add(currentPhaseIndex+1, clazz);
    }

    /**
     * Removes the next phase
     */
    public void removeNextPhase(){
        this.phaseLinkedList.remove(currentPhaseIndex+1);
    }

    /**
     * Adds phase to the last
     * @param clazz Phase you want to add
     */
    public void addPhase(Class<? extends AbstractPhase> clazz){
        this.phaseLinkedList.addLast(clazz);
    }

    /**
     * Removes the last phase
     */
    public void removeLastPhase(){
        this.phaseLinkedList.removeLast();
    }

    /**
     * Gets called when the game ends
     */
    public void onDisable(){

    }

    /**
     * Gets called every tick
     */
    public void onTick(){}

    /**
     * Disables the minigame
     */
    public final void disable(){
        plugin.getServer().getPluginManager().callEvent(new GameFinishEvent(this));
        if(this.currentPhase!=null)currentPhase.disable();
        Bukkit.getScheduler().cancelTask(taskNumber);
        onDisable();
    }

    /**
     * tick method
     */
    protected final void tick(){
        if(getCurrentPhase()!=null)getCurrentPhase().tick();
        if(!frozen&&(getCurrentPhase()!=null&&getCurrentPhase().timeLeft().isZero())) endPhase();
        onTick();
    }

    /**
     * Returns current phase
     * @return Current phase
     */
    public AbstractPhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     *
     * @return is minigame frozen
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Sets minigame's frozen state
     * @param frozen frozen state
     */
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    /**
     *
     * @return plugin class
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Returns game data
     * @return game data
     */
    public GameData getGameData() {
        return gameData;
    }

    /**
     * Sets the gamedata to game data you want
     * @param gameData GameData
     */
    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }
}
