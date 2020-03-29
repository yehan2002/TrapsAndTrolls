package io.github.yehan2002.Traps.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;


@SuppressWarnings("unused")
public final class TrapTriggeredEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private TrapManager Trap;
    private boolean cancelled;
    private Player player;
    private String Custom;
    private boolean remove = true;

    public TrapTriggeredEvent(Player p, TrapManager trap, String custom) {
        this.Trap = trap;
        this.player = p;
        this.Custom  = custom;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean isRemove() {
        return remove;
    }

    public boolean isCustom(){return Custom != null;}

    public String getCustom(){return Custom;}

    public Player getPlayer() {
        return player;
    }

    public TrapManager getTrap() {
        return Trap;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
