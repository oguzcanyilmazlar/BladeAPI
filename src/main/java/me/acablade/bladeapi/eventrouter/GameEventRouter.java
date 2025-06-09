package me.acablade.bladeapi.eventrouter;

import lombok.Setter;
import me.acablade.bladeapi.data.IPlayerData;
import me.acablade.bladeapi.events.GameEvent;
import me.acablade.bladeapi.objects.GameContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class GameEventRouter {

    private static final Logger LOGGER = Logger.getLogger("BladeAPI-GameEventRouter");


    private final GameContext context;
    private final Plugin plugin;
    private final Map<Class<? extends Event>, List<RegisteredHandler<? extends Event>>> handlers = new HashMap<>();

    @Setter
    private GameEventRegistrationPolicy registrationPolicy = (eventClass, owner) -> true;

    public GameEventRouter(Plugin plugin, GameContext context) {
        this.context = context;
        this.plugin = plugin;
    }

    public <T extends Event> void listen(Class<T> eventClass, Consumer<T> handler, Object owner) {
        listen(eventClass, handler, owner, EventPriority.NORMAL, false);
    }

    public <T extends Event> void listen(Class<T> eventClass, Consumer<T> handler, Object owner, EventPriority priority, boolean ignoreCancelled) {
        if (!registrationPolicy.canRegister(eventClass, owner)) {
            throw new IllegalStateException("Feature " + owner.getClass().getSimpleName() +
                    " is not allowed to register handler for " + eventClass.getSimpleName());
        }
        RegisteredHandler<T> regHandler = new RegisteredHandler<>(handler);

        handlers.computeIfAbsent(eventClass, k -> {
            registerWithBukkit(k, priority, ignoreCancelled);
            return new ArrayList<>();
        }).add(regHandler);
    }

    private <T extends Event> void registerWithBukkit(Class<T> eventClass, EventPriority priority, boolean ignoreCancelled) {
        Bukkit.getPluginManager().registerEvent(eventClass, new Listener() {}, priority, (listener, event) -> {
            //noinspection unchecked
            T evt = (T) event;
            if (!isRelevantEvent(evt)) return;

            List<RegisteredHandler<? extends Event>> list = handlers.get(eventClass);
            if (list != null) {
                for (RegisteredHandler<? extends Event> handler : list) {
                    try {
                        ((RegisteredHandler<T>) handler).invoke(evt);
                    } catch (Exception ex) {
                        LOGGER.severe("Error invoking event(" + evt.getClass().getName() + "):");
                        ex.printStackTrace();
                    }
                }
            }
        }, plugin, ignoreCancelled);
    }

    private boolean isRelevantEvent(Event event) {

        if (event instanceof GameEvent gameEvent) {
            return gameEvent.getGame().equals(context.getGame());
        }

        IPlayerData playerData;
        try {
            playerData = context.getPlayerData();
        } catch (IllegalStateException e) {
            return false;
        }

        Set<UUID> eventPlayers = extractPlayersFromEvent(event);

        if (eventPlayers.isEmpty()) return false;
        for (UUID playerId : eventPlayers) {
            if (playerData.getAllPlayers().contains(playerId)) {
                return true;
            }
        }

        return false;
    }

    private Set<UUID> extractPlayersFromEvent(Event event) {
        Set<UUID> players = new HashSet<>();

        try {
            switch (event) {

                case InventoryInteractEvent inventoryInteractEvent ->
                        players.add(inventoryInteractEvent.getWhoClicked().getUniqueId());
                case BlockBreakEvent blockBreakEvent -> players.add(blockBreakEvent.getPlayer().getUniqueId());
                case BlockPlaceEvent blockPlaceEvent -> players.add(blockPlaceEvent.getPlayer().getUniqueId());
                case EntityDamageByEntityEvent damageEvent -> {
                    if (damageEvent.getEntity() instanceof Player damaged) {
                        players.add(damaged.getUniqueId());
                    }
                    Entity damager = damageEvent.getDamager();
                    if (damager instanceof Player playerDamager) {
                        players.add(playerDamager.getUniqueId());
                    } else if (damager instanceof Projectile projectile) {
                        if (projectile.getShooter() instanceof Player shooter) {
                            players.add(shooter.getUniqueId());
                        }
                    } else if (damager instanceof TNTPrimed tnt) {
                        if (tnt.getSource() instanceof Player sourcePlayer) {
                            players.add(sourcePlayer.getUniqueId());
                        }
                    }
                }
                case PlayerInteractEntityEvent interactEvent -> {
                    players.add(interactEvent.getPlayer().getUniqueId());
                    if (interactEvent.getRightClicked() instanceof Player clickedPlayer) {
                        players.add(clickedPlayer.getUniqueId());
                    }
                }
                case PlayerEvent playerEvent -> players.add(playerEvent.getPlayer().getUniqueId());
                case EntityEvent entityEvent -> {
                    Entity entity = entityEvent.getEntity();
                    if (entity instanceof Player) {
                        players.add(entity.getUniqueId());
                    }
                }
                case null, default -> LOGGER.warning("Unhandled event type: " + event.getClass().getName());
            }
        } catch (Exception ignored) {
            LOGGER.severe("something went very wrong.");
        }

        return players;
    }

    public void clearAll() {
        for (List<RegisteredHandler<? extends Event>> handlerList : handlers.values()) {
            handlerList.clear();
        }
        handlers.clear();
    }

    private record RegisteredHandler<T extends Event>(Consumer<T> consumer) {

        public void invoke(T event) {
                consumer.accept(event);
            }
    }
}
