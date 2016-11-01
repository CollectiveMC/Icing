package org.cyberpwn.icing.xp;

import org.bukkit.entity.Player;
import org.phantomapi.event.CancellablePhantomEvent;

public class XPEvent extends CancellablePhantomEvent
{
	private final XPReason reason;
	private final Player player;
	private long xp;
	
	public XPEvent(Player player, long xp, XPReason reason)
	{
		this.xp = xp;
		this.player = player;
		this.reason = reason;
	}
	
	public long getXp()
	{
		return xp;
	}
	
	public void setXp(long xp)
	{
		this.xp = xp;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public XPReason getReason()
	{
		return reason;
	}
}
