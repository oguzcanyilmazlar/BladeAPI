package me.acablade.bladeapi.eventbus;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class EventBus{
	
	public static <T extends Event> void listen(Class<T> clazz, Listener listener, EventPriority prio, Consumer<T> consumer, JavaPlugin plugin, boolean ignoreCancelled){
		Bukkit.getPluginManager().registerEvent(clazz, listener, prio, (list, event) -> {
			consumer.accept((T) event);
		}, plugin, ignoreCancelled);
	}
	
	
	public static <T extends Event> void listen(Class<T> clazz, Listener listener, Consumer<T> consumer, JavaPlugin plugin){
		listen(clazz, listener, EventPriority.NORMAL, consumer, plugin, false);
	}
	
	public static <T extends Event> void listen(Class<T> clazz, Listener listener, Consumer<T> consumer, JavaPlugin plugin, boolean ignoreCancelled){
		listen(clazz, listener, EventPriority.NORMAL, consumer, plugin, ignoreCancelled);
	}
	
	public static <T extends Event> void listen(Class<T> clazz, Listener listener, EventPriority prio, Consumer<T> consumer, JavaPlugin plugin){
		listen(clazz, listener, prio, consumer, plugin, false);
	}
	
	
}
