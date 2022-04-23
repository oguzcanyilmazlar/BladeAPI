package me.acablade.bladeapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
@Getter
@Setter
public abstract class AbstractGame {

    private final String name;
    private final JavaPlugin plugin;

    private int taskNumber = -1;

    private int currentPhaseIndex = 0;
    private AbstractPhase currentPhase;

    private boolean frozen;

    private final LinkedList<Class<? extends AbstractPhase>> phaseLinkedList = new LinkedList<>();
    private final GameData gameData = new GameData();

    public void onEnable(){

    }

    public void endPhase(){
        if(isFrozen()) return;
        try {
            if(this.currentPhase!=null)this.currentPhase.disable();
            if(currentPhaseIndex>=phaseLinkedList.size()) {
                disable();
                return;
            }
            AbstractPhase phase = phaseLinkedList.get(currentPhaseIndex).getDeclaredConstructor(AbstractGame.class).newInstance(this);
            phase.enable();
            this.currentPhase = phase;
            this.currentPhaseIndex++;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // TODO: remove
            e.printStackTrace();
        }
    }

    public final void enable(long delay, long tick){
        if(taskNumber>0) return;
        onEnable();
        endPhase();
        taskNumber = (new BukkitRunnable(){
            @Override
            public void run() {
                tick();
            }
        }).runTaskTimer(plugin,delay,tick).getTaskId();
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


}
