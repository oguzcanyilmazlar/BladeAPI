package me.acablade.bladeapi;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.acablade.bladeapi.events.GameFinishEvent;
import me.acablade.bladeapi.events.GameStartEvent;
import me.acablade.bladeapi.events.GameStateChangeEvent;
import me.acablade.bladeapi.events.GameTickEvent;
import me.acablade.bladeapi.objects.IGameData;

@RequiredArgsConstructor
public abstract class AbstractGame implements IGame{


    @Getter
    private final String name;
    @Getter
    private final JavaPlugin plugin;

    private final LinkedList<IState> phaseLinkedList = new LinkedList<>();

    private int taskNumber = -1;

    private int currentPhaseIndex = 0;

    @Getter
    @Setter
    private long period = -1;


    @Getter
    @Setter
    private IState currentPhase;

    @Getter
    @Setter
    private boolean frozen;

    public void onEnable(){}

    public void onDisable(){}

    public void onTick(){}

    @Override
    public IGameData getGameData() {
        return null;
    }

    public void endPhase(){
        if(isFrozen()) return;
        if(currentPhaseIndex>=phaseLinkedList.size()) {
            disable();
            return;
        }
        IState phase = phaseLinkedList.get(currentPhaseIndex);
        GameStateChangeEvent phaseChangeEvent = new GameStateChangeEvent(this, this.currentPhase,phase);
        Bukkit.getPluginManager().callEvent(phaseChangeEvent);
        if(phaseChangeEvent.isCancelled()) return;
        if(this.currentPhase!=null)this.currentPhase.disable();
        phase.enable();
        this.currentPhase = phase;
        this.currentPhaseIndex++;
    }

    public void enable(long delay, long period){
        if(taskNumber>0) return;
        plugin.getServer().getPluginManager().callEvent(new GameStartEvent(this));
        onEnable();
        endPhase();
        taskNumber = Bukkit.getScheduler().runTaskTimer(plugin,this::tick,delay,period).getTaskId();
        this.period = period;
    }

    public void addPhaseNext(IState phase){
        this.phaseLinkedList.add(currentPhaseIndex+1, phase);
    }

    public void removeNextPhase(){
        this.phaseLinkedList.remove(currentPhaseIndex+1);
    }

    public void addPhase(IState phase){
        this.phaseLinkedList.addLast(phase);
    }

    public void removeLastPhase(){
        this.phaseLinkedList.removeLast();
    }

    public void disable(){
        plugin.getServer().getPluginManager().callEvent(new GameFinishEvent(this));
        if(this.currentPhase!=null)currentPhase.disable();
        Bukkit.getScheduler().cancelTask(taskNumber);
        onDisable();
    }

    public IState getCurrentPhase(){
        return this.phaseLinkedList.get(this.currentPhaseIndex);
    }

    protected void tick(){
    	plugin.getServer().getPluginManager().callEvent(new GameTickEvent(this));
        if(getCurrentPhase()!=null)getCurrentPhase().tick();
        if(!frozen&&(getCurrentPhase()!=null&&getCurrentPhase().timeLeft().isZero())) endPhase();
        onTick();
    }
}
