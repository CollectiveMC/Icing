package org.cyberpwn.icing.xp;

import org.bukkit.entity.Player;
import org.cyberpwn.icing.Icing;
import org.phantomapi.util.D;
import org.phantomapi.util.F;

public class XP
{
	public static void giveXp(Player player, long xp, XPReason reason)
	{
		XPEvent e = new XPEvent(player, xp, reason);
		Icing.inst().callEvent(e);
		
		if(!e.isCancelled())
		{
			Icing.inst().getXp().getXpDataController().get(player).setXp(e.getXp() + getXp(player));
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
	
	public static double percentToNextLevel(long xp)
	{
		double level = getLevelForXp(xp);
		double axp = getXpForLevel((long) level);
		double bxp = getXpForLevel((long) (level + 1));
		double cxp = xp - axp;
		double dxp = bxp - axp;
		return cxp / dxp;
	}
	
	public static long xpToNextLevel(long xp)
	{
		double level = getLevelForXp(xp);
		double axp = getXpForLevel((long) level);
		double bxp = getXpForLevel((long) (level + 1));
		double cxp = xp - axp;
		double dxp = bxp - axp;
		
		return (long) (dxp - cxp);
	}
	
	public static long getXpForLevel(long level)
	{
		return (long) Math.pow((double) level, 3);
	}
	
	public static long getLevelForXp(long xp)
	{
		return (long) Math.cbrt((double) xp);
	}
	
	public static void printLeveling(int maxLevel)
	{
		long vxp = getXpForLevel(1);
		long vle = 1;
		D d = new D("X");
		
		while(vle < maxLevel)
		{
			vle = getLevelForXp(vxp);
			vxp = getXpForLevel(vle + 1);
			d.s("Level " + F.f(vle) + " needs " + F.f(vxp - getXpForLevel(vle)) + " XP, Total of " + F.f(getXpForLevel(vle)) + " XP");
		}
	}
}
