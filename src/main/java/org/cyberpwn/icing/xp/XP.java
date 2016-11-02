package org.cyberpwn.icing.xp;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.Icing;
import org.inventivetalent.bossbar.BossBarAPI;
import org.phantomapi.Phantom;
import org.phantomapi.gui.Notification;
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.Priority;
import org.phantomapi.lang.Title;
import org.phantomapi.util.C;
import org.phantomapi.util.D;
import org.phantomapi.util.F;

public class XP
{
	@SuppressWarnings("deprecation")
	public static void giveXp(Player player, long xp, XPReason reason)
	{
		XPEvent e = new XPEvent(player, xp, reason);
		Icing.inst().callEvent(e);
		
		if(!e.isCancelled())
		{
			long level = getLevelForXp(getXp(player));
			Icing.inst().getXp().getXpDataController().get(player).setXp(e.getXp() + getXp(player));
			long levelNext = getLevelForXp(getXp(player));
			BossBarAPI.removeAllBars(player);
			BossBarAPI.setMessage(player, C.LIGHT_PURPLE + "Level " + XP.getLevelForXp(getXp(player)), (float) ((float) XP.percentToNextLevel(getXp(player)) * 100.0), -1);
			
			if(levelNext > level)
			{
				Notification n = new Notification();
				Title t = new Title();
				t.setTitle("   ");
				t.setSubTitle(C.DARK_GRAY + "Level " + C.LIGHT_PURPLE + levelNext);
				t.setAction("   ");
				t.setFadeIn(5);
				t.setStayTime(15);
				t.setFadeOut(20);
				n.setTitle(t);
				n.setAudible(new GSound(Sound.WITHER_DEATH, 1f, 1.98f));
				n.setPriority(Priority.LOW);
				Phantom.queueNotification(player, n);
			}
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
		return (long) Math.pow((double) level, 4);
	}
	
	public static long getLevelForXp(long xp)
	{
		return (long) Math.pow(xp, 0.25);
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
