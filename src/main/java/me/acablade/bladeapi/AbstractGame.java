package me.acablade.bladeapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.acablade.bladeapi.events.GameFinishEvent;
import me.acablade.bladeapi.events.GamePhaseChangeEvent;
import me.acablade.bladeapi.events.GameStartEvent;
import me.acablade.bladeapi.objects.BukkitActor;
import me.acablade.bladeapi.objects.IActor;
import me.acablade.bladeapi.objects.IGameData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class AbstractGame implements IGame{


    @Getter
    private final String name;
    @Getter
    private final JavaPlugin plugin;

    private final LinkedList<IPhase> phaseLinkedList = new LinkedList<>();

    private int taskNumber = -1;

    private int currentPhaseIndex = 0;

    @Getter
    @Setter
    private long period = -1;


    @Getter
    @Setter
    private IPhase currentPhase;

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
        IPhase phase = phaseLinkedList.get(currentPhaseIndex);
        GamePhaseChangeEvent phaseChangeEvent = new GamePhaseChangeEvent(this, this.currentPhase,phase);
        Bukkit.getPluginManager().callEvent(phaseChangeEvent);
        if(phaseChangeEvent.isCancelled()) return;
        if(this.currentPhase!=null)this.currentPhase.disable();
        phase.enable();
        this.currentPhase = phase;
        this.currentPhaseIndex++;
    }

    public final void enable(long delay, long period){
        if(taskNumber>0) return;
        plugin.getServer().getPluginManager().callEvent(new GameStartEvent(this));
        onEnable();
        endPhase();
        taskNumber = Bukkit.getScheduler().runTaskTimer(plugin,this::tick,delay,period).getTaskId();
        this.period = period;
    }

    public void addPhaseNext(IPhase phase){
        this.phaseLinkedList.add(currentPhaseIndex+1, phase);
    }

    public void removeNextPhase(){
        this.phaseLinkedList.remove(currentPhaseIndex+1);
    }

    public void addPhase(IPhase phase){
        this.phaseLinkedList.addLast(phase);
    }

    public void removeLastPhase(){
        this.phaseLinkedList.removeLast();
    }

    public final void disable(){
        plugin.getServer().getPluginManager().callEvent(new GameFinishEvent(this));
        if(this.currentPhase!=null)currentPhase.disable();
        Bukkit.getScheduler().cancelTask(taskNumber);
        onDisable();
    }

    public IPhase getCurrentPhase(){
        return this.phaseLinkedList.get(this.currentPhaseIndex);
    }

    protected final void tick(){
        if(getCurrentPhase()!=null)getCurrentPhase().tick();
        if(!frozen&&(getCurrentPhase()!=null&&getCurrentPhase().timeLeft().isZero())) endPhase();
        onTick();
    }

    public Stream<IActor> getAllPlayers(){

        return getGameData().getActors().stream();
    }

    public void announce(String msg){
        getAllPlayers().forEach(player -> player.sendMessage(msg));
    }

    public void addActor(IActor actor){
        this.getGameData().addActor(actor);
    }

    public void removeActor(IActor actor){
        this.getGameData().removeActor(actor);
    }
}
