package org.cyberpwn.icing.xp;

import org.bukkit.entity.Player;
import org.cyberpwn.icing.Icing;

public class XP
{
	public static void giveXp(Player player, long xp, XPReason reason)
	{
		XPEvent e = new XPEvent(player, xp, reason);
		Icing.inst().callEvent(e);
		
		if(!e.isCancelled())
		{
			Icing.inst().getXp().getXpDataController().get(player).setXp(e.getXp());
		}
	}
	
	public static long getXp(Player player)
	{
		return Icing.inst().getXp().getXpDataController().get(player).getXp();
	}
	
	public static void setXp(Player player, long xp)
	{
		Icing.inst().getXp().getXpDataController().get(player).setXp(xp);
	}
	
	public static double getBoost(Player player)
	{
		return Icing.inst().getXp().getXpDataController().get(player).getBoost();
	}
	
	public static void setBoost(Player player, double boost)
	{
		Icing.inst().getXp().getXpDataController().get(player).setBoost(boost);
	}
}
