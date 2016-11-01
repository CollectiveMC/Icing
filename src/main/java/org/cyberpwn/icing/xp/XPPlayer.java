package org.cyberpwn.icing.xp;

import org.bukkit.entity.Player;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.Keyed;
import org.phantomapi.clust.Tabled;

@Tabled("ice_xp_player")
public class XPPlayer extends ConfigurableObject
{
	private Player player;
	
	@Keyed("x")
	public long xp = 0;
	
	@Keyed("b")
	public double boost = 0;
	
	public XPPlayer(Player player)
	{
		super(player.getUniqueId().toString());
		
		this.player = player;
	}
	
	public long getXp()
	{
		return xp;
	}
	
	public void setXp(long xp)
	{
		this.xp = xp;
	}
	
	public double getBoost()
	{
		return boost;
	}
	
	public void setBoost(double boost)
	{
		this.boost = boost;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public void modXp(long amt)
	{
		setXp(getXp() + amt);
	}
	
	public void modBoost(double amt)
	{
		setBoost(getBoost() + amt);
	}
}
