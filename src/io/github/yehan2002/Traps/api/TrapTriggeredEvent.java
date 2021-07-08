package io.github.yehan2002.Traps.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;


@SuppressWarnings("unused")
public final class TrapTriggeredEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final TrapManager Trap;
    @Getter
    private final Player player;
    @Getter
    private final String Custom;
    @Getter @Setter
    private boolean remove = true;
    @Getter @Setter
    private boolean cancelled;

    public TrapTriggeredEvent(Player p, TrapManager trap, String custom) {
        this.Trap = trap;
        this.player = p;
        this.Custom  = custom;
    }

    public boolean isCustom(){return Custom != null;}

    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
