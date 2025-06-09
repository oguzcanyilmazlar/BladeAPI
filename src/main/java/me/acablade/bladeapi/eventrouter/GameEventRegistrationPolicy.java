package me.acablade.bladeapi.eventrouter;

import org.bukkit.event.Event;

public interface GameEventRegistrationPolicy {
    boolean canRegister(Class<? extends Event> eventClass, Object owner);
}
