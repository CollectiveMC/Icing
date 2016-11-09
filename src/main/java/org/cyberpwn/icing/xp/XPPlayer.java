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
	
	@Keyed("bt")
	public int boosterTicks = 0;
	
	@Keyed("ba")
	public double boosterAmount = 0;
	
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
	
	public int getBoosterTicks()
	{
		return boosterTicks;
	}
	
	public void setBoosterTicks(int boosterTicks)
	{
		this.boosterTicks = boosterTicks;
	}
	
	public double getBoosterAmount()
	{
		return boosterAmount;
	}
	
	public void setBoosterAmount(double boosterAmount)
	{
		this.boosterAmount = boosterAmount;
	}
}
