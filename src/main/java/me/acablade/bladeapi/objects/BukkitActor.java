package me.acablade.bladeapi.objects;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

//wrapper

@Data
public class BukkitActor implements IActor{

    public static BukkitActor of(UUID uuid){
        return new BukkitActor(Bukkit.getPlayer(uuid));
    }

    public static BukkitActor of(Player player){
        return new BukkitActor(player);
    }


    private final Player player;

}
