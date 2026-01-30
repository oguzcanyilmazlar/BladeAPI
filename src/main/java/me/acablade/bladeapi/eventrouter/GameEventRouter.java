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
import java.util.function.Function;
import java.util.logging.Logger;

public class GameEventRouter {

    private static final Logger LOGGER = Logger.getLogger("BladeAPI-GameEventRouter");

    private static final Map<Class<? extends Event>, Function<Event, Set<UUID>>> PLAYER_EXTRACTORS = new LinkedHashMap<>();

    static {
        registerExtractor(PlayerEvent.class, event ->
                Set.of((event).getPlayer().getUniqueId()));

        registerExtractor(InventoryInteractEvent.class, event ->
                Set.of((event).getWhoClicked().getUniqueId()));

        registerExtractor(BlockBreakEvent.class, event ->
                Set.of(( event).getPlayer().getUniqueId()));

        registerExtractor(BlockPlaceEvent.class, event ->
                Set.of((event).getPlayer().getUniqueId()));

        registerExtractor(PlayerInteractEntityEvent.class, event -> {
            Set<UUID> ids = new HashSet<>();
            ids.add(event.getPlayer().getUniqueId());
            if (event.getRightClicked() instanceof Player p) {
                ids.add(p.getUniqueId());
            }
            return ids;
        });

        registerExtractor(EntityDamageByEntityEvent.class, event -> {
            Set<UUID> ids = new HashSet<>();


            if (event.getEntity() instanceof Player damaged)
                ids.add(damaged.getUniqueId());

            Entity damager = event.getDamager();
            switch (damager) {
                case Player p -> ids.add(p.getUniqueId());
                case Projectile projectile -> {
                    if (projectile.getShooter() instanceof Player shooter) {
                        ids.add(shooter.getUniqueId());
                    }
                }
                case TNTPrimed tnt -> {
                    if (tnt.getSource() instanceof Player source) {
                        ids.add(source.getUniqueId());
                    }
                }
                default -> {
                }
            }

            return ids;
        });

        registerExtractor(EntityEvent.class, event -> {
            Entity entity = event.getEntity();
            return (entity instanceof Player p) ? Set.of(p.getUniqueId()) : Set.of();
        });
    }

    public static <T extends Event> void registerExtractor(Class<T> clazz, Function<T, Set<UUID>> extractor) {
        PLAYER_EXTRACTORS.putIfAbsent(clazz, (Function<Event, Set<UUID>>) extractor);
    }


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

        if(PLAYER_EXTRACTORS.containsKey(event.getClass())){
            try {
                return PLAYER_EXTRACTORS.get(event.getClass()).apply(event);
            } catch (Exception e) {
                LOGGER.severe("Error extracting players from event: " + event.getClass().getSimpleName());
                e.printStackTrace();
            }
        }

        for (Map.Entry<Class<? extends Event>, Function<Event, Set<UUID>>> entry : PLAYER_EXTRACTORS.entrySet()) {
            if (event.getClass().isAssignableFrom(entry.getKey())) {
                try {
                    return entry.getValue().apply(event);
                } catch (Exception e) {
                    LOGGER.severe("Error extracting players from event: " + event.getClass().getSimpleName());
                    e.printStackTrace();
                }
            }
        }

        return Collections.emptySet();
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
