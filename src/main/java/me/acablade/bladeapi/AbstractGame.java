package me.acablade.bladeapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.acablade.bladeapi.events.GameFinishEvent;
import me.acablade.bladeapi.events.GamePhaseChangeEvent;
import me.acablade.bladeapi.events.GameStartEvent;
import me.acablade.bladeapi.objects.BukkitActor;
import me.acablade.bladeapi.objects.GameData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

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

    @Getter
    @Setter
    private GameData gameData = new GameData();

    public void onEnable(){}

    public void onDisable(){}

    public void onTick(){}



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

    public void addPhaseNext(AbstractPhase phase){
        this.phaseLinkedList.add(currentPhaseIndex+1, phase);
    }

    public void removeNextPhase(){
        this.phaseLinkedList.remove(currentPhaseIndex+1);
    }

    public void addPhase(AbstractPhase phase){
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

    protected final void tick(){
        if(getCurrentPhase()!=null)getCurrentPhase().tick();
        if(!frozen&&(getCurrentPhase()!=null&&getCurrentPhase().timeLeft().isZero())) endPhase();
        onTick();
    }

    public final Collection<Player> allPlayers(){
        Collection<Player> players = new ArrayList<>();
        this.gameData.getPlayers().stream().map(BukkitActor::getPlayer).forEach(players::add);
        this.gameData.getSpectators().stream().map(BukkitActor::getPlayer).forEach(players::add);
        return players;
    }

    public void announce(String msg){
        allPlayers().forEach(player -> player.sendMessage(msg));
    }

    public void addPlayer(Player player){
        this.gameData.getPlayers().add(BukkitActor.of(player));
    }
    public void addPlayer(UUID uuid){
        this.addPlayer(Bukkit.getPlayer(uuid));
    }

    public void removePlayer(Player player){
        this.gameData.getPlayers().remove(BukkitActor.of(player));
    }
    public void removePlayer(UUID uuid){
        this.removePlayer(Bukkit.getPlayer(uuid));
    }


    public void addSpectator(Player player){
        this.gameData.getSpectators().add(BukkitActor.of(player));
    }
    public void addSpectator(UUID uuid){
        this.addSpectator(Bukkit.getPlayer(uuid));
    }

    public void removeSpectator(Player player){
        this.gameData.getSpectators().remove(BukkitActor.of(player));
    }
    public void removeSpectator(UUID uuid){
        this.removeSpectator(Bukkit.getPlayer(uuid));
    }
}
