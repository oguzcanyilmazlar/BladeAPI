package me.acablade.bladeapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.acablade.bladeapi.events.GameFinishEvent;
import me.acablade.bladeapi.events.GamePhaseChangeEvent;
import me.acablade.bladeapi.events.GameStartEvent;
import me.acablade.bladeapi.objects.GameData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.UUID;

/**
 * @author Acablade/oz
 */

@RequiredArgsConstructor
public abstract class AbstractGame<T extends JavaPlugin> {

    /**
     * Game name
     */
    @Getter
    private final String name;

    /**
     * Main plugin
     */
    private final T plugin;

    /**
     * Task number so the game doesn't get enabled more than once
     */
    private int taskNumber = -1;

    private int currentPhaseIndex = 0;

    @Getter
    private long period = -1;

    private AbstractPhase currentPhase;

    private boolean frozen;

    private final LinkedList<AbstractPhase> phaseLinkedList = new LinkedList<>();

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
        if(currentPhaseIndex>=phaseLinkedList.size()) {
            disable();
            return;
        }
        AbstractPhase phase = phaseLinkedList.get(currentPhaseIndex);
        GamePhaseChangeEvent phaseChangeEvent = new GamePhaseChangeEvent(this, this.currentPhase,phase);
        Bukkit.getPluginManager().callEvent(phaseChangeEvent);
        if(phaseChangeEvent.isCancelled()) return;
        if(this.currentPhase!=null)this.currentPhase.disable();
        phase.enable();
        this.currentPhase = phase;
        this.currentPhaseIndex++;
    }

    /**
     * Starts the game
     */
    public final void enable(long delay, long period){
        if(taskNumber>0) return;
        plugin.getServer().getPluginManager().callEvent(new GameStartEvent(this));
        onEnable();
        endPhase();
        taskNumber = Bukkit.getScheduler().runTaskTimer(plugin,this::tick,delay,period).getTaskId();
        this.period = period;
    }

    /**
     * Adds a phase directly after the current phase
     */
    public void addPhaseNext(AbstractPhase phase){
        this.phaseLinkedList.add(currentPhaseIndex+1, phase);
    }

    /**
     * Removes the next phase
     */
    public void removeNextPhase(){
        this.phaseLinkedList.remove(currentPhaseIndex+1);
    }

    /**
     * Adds phase to the last
     */
    public void addPhase(AbstractPhase phase){
        this.phaseLinkedList.addLast(phase);
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
     * @return Current phase
     */
    public AbstractPhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * @return is minigame frozen
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Sets minigame's frozen state
     */
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * @return plugin class
     */
    public T getPlugin() {
        return plugin;
    }

    /**
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

    public void announce(String msg){
        getGameData().allPlayers().forEach(player -> player.sendMessage(msg));

    }

    public void addPlayer(UUID uuid){
        this.gameData.getPlayerList().add(uuid);
    }

    public void removePlayer(UUID uuid){
        this.gameData.getPlayerList().remove(uuid);
    }

    public void addSpectator(UUID uuid){
        this.gameData.getSpectatorList().add(uuid);
    }

    public void removeSpectator(UUID uuid){
        this.gameData.getSpectatorList().remove(uuid);
    }
}
