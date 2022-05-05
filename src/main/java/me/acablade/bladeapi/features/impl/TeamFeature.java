package me.acablade.bladeapi.features.impl;

import lombok.Getter;
import me.acablade.bladeapi.AbstractPhase;
import me.acablade.bladeapi.events.TeamChooseEvent;
import me.acablade.bladeapi.features.AbstractFeature;
import me.acablade.bladeapi.objects.GamePlayer;
import me.acablade.bladeapi.objects.Team;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Acablade/oz
 */

@Getter
public class TeamFeature extends AbstractFeature {

    private Set<Team> teamSet = new HashSet<>();
    private final boolean friendlyFire;

    private final TeamFeatureSupplier teamFeatureSupplier;

    private static final TeamFeatureSupplier DEFAULT_TEAM_SUPPLIER = () -> {
        Set<Team> teamSet = new HashSet<>();

        List<ChatColor> colorList = new ArrayList<>();

        colorList.add(ChatColor.BLUE);
        colorList.add(ChatColor.GOLD);
        colorList.add(ChatColor.DARK_RED);
        colorList.add(ChatColor.DARK_PURPLE);
        colorList.add(ChatColor.WHITE);
        colorList.add(ChatColor.BLACK);

        List<String> nameList = colorList.stream().map(ChatColor::name).collect(Collectors.toList());

        for (int i = 0; i < colorList.size(); i++) {
            teamSet.add(new Team(nameList.get(i), colorList.get(i), 2));
        }

        return teamSet;
    };

    public TeamFeature(AbstractPhase abstractPhase) {
        this(abstractPhase,DEFAULT_TEAM_SUPPLIER);
    }

    public TeamFeature(AbstractPhase phase, TeamFeatureSupplier supplier){
        this(phase,supplier,false);
    }

    public TeamFeature(AbstractPhase phase, TeamFeatureSupplier supplier, boolean friendlyFire){
        super(phase);
        this.teamFeatureSupplier = supplier;
        this.friendlyFire = friendlyFire;
    }



    @Override
    public void onEnable(){

        teamSet = teamFeatureSupplier.supplyTeams();

        Set<Player> playerSet = getAbstractPhase().getGame().getGameData().getPlayerList().stream().map(Bukkit::getPlayer).collect(Collectors.toSet());

        Map<String, Integer> sizeMap = calcSizes();


        for(Player player: playerSet){
            Team team = getTeam(getSmallestTeam(sizeMap)).orElseThrow(() -> new RuntimeException("Takım bulunamadı!"));
            TeamChooseEvent event = new TeamChooseEvent(player, getAbstractPhase().getGame(), team);
            Bukkit.getPluginManager().callEvent(event);
            if(event.isCancelled()) continue;
            team.getPlayerList().add(player.getUniqueId());
        }


    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(isFriendlyFire()) return;

        Entity damager = event.getDamager();
        Entity victim = event.getEntity();

        if(!(damager instanceof Player) && !(victim instanceof Player)) return;

        GamePlayer damagerGamePlayer = GamePlayer.getGamePlayer(damager.getUniqueId());
        GamePlayer victimGamePlayer = GamePlayer.getGamePlayer(victim.getUniqueId());

        event.setCancelled(damagerGamePlayer.getTeam()==victimGamePlayer.getTeam());

    }

    private Map<String, Integer> calcSizes(){
        Map<String, Integer> map = new HashMap<>();

        for(Team team: teamSet){
            map.put(team.getName(),team.getPlayerList().size());
        }

        return map;
    }

    private String getSmallestTeam(Map<String, Integer> sizes){

        int largest = 4;
        String name = "";

        for (final String s : sizes.keySet()) {
            final int x = sizes.get(s);
            if (x <= largest) {
                largest = x;
                name = s;
            }
        }
        return name;

    }

    public Optional<Team> getTeam(String name) {
        return teamSet.stream().filter(team -> team.getName().equalsIgnoreCase(name)).findFirst();
    }
}
