package me.acablade.bladeapi;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

import me.acablade.bladeapi.objects.GameContext;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import me.acablade.bladeapi.events.GameFinishEvent;
import me.acablade.bladeapi.events.GameStartEvent;
import me.acablade.bladeapi.events.GameStateChangeEvent;
import me.acablade.bladeapi.events.GameTickEvent;

public abstract class AbstractGame implements IGame{

    @Getter
    private final String name;
    @Getter
    private final JavaPlugin plugin;

    private final Queue<IState> stateQueue = new ArrayDeque<>();

    private int taskNumber = -1;

    @Getter
    @Setter
    private long period = -1;


    @Setter
    private IState currentState;

    @Setter
    private boolean frozen;

    @Getter
    private final GameContext gameContext;

    private boolean statesLocked = false;
    public AbstractGame(String name, JavaPlugin plugin) {
        this.name = name;
        this.plugin = plugin;
        this.gameContext = new GameContext(plugin, this);
    }


    public boolean isFrozen() {
        return frozen;
    }

    @Override
    public IState getCurrentState() {
        return currentState;
    }

    @Override
    public void onEnable(){}

    @Override
    public void onDisable(){}

    @Override
    public void onTick(){
        assertMainThread();
    }

    private void assertMainThread() {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Game state can only be modified from the main server thread.");
        }
    }

    public void endPhase() {
        assertMainThread();
        if (isFrozen()) return;

        if (currentState != null) {
            currentState.disable();
        }

        IState prevPhase = currentState;
        IState tmpNextPhase = stateQueue.peek();

        GameStateChangeEvent changeEvent = new GameStateChangeEvent(this, prevPhase, tmpNextPhase);
        Bukkit.getPluginManager().callEvent(changeEvent);
        if (changeEvent.isCancelled()) return;

        currentState = changeEvent.getNextPhase();

        if (currentState == null) {
            disable();
            return;
        }

        if (Objects.equals(tmpNextPhase, currentState)) {
            stateQueue.poll();
        }


        currentState.enable();
    }

    @Override
    public void enable(long delay, long period){
        assertMainThread();
        if (statesLocked) throw new IllegalStateException("Game already started, states cannot be modified.");
        if(taskNumber != -1) return;
        plugin.getServer().getPluginManager().callEvent(new GameStartEvent(this));
        onEnable();
        endPhase();
        taskNumber = Bukkit.getScheduler().runTaskTimer(plugin,this::tick,delay,period).getTaskId();
        this.period = period;
        statesLocked = true;
    }

    @Override
    public void disable() {
        assertMainThread();
        gameContext.getEventRouter().clearAll();
        plugin.getServer().getPluginManager().callEvent(new GameFinishEvent(this));
        if(this.currentState !=null) currentState.disable();
        Bukkit.getScheduler().cancelTask(taskNumber);
        this.taskNumber = -1;
        onDisable();
    }

    @Override
    public void addState(IState state) {
        assertMainThread();
        if (state == null) throw new IllegalArgumentException("State cannot be null.");
        if(statesLocked) throw new IllegalStateException("Cannot add state after game has started.");
        stateQueue.offer(state);
    }

    protected void tick(){
    	plugin.getServer().getPluginManager().callEvent(new GameTickEvent(this));
        IState currentState = getCurrentState();
        if (currentState == null) return;
        currentState.tick();
        if(!frozen && currentState.timeLeft().isZero()) endPhase();
        onTick();
    }
}


