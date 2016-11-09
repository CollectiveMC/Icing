package org.cyberpwn.icing.xp;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cyberpwn.icing.Icing;
import org.inventivetalent.bossbar.BossBarAPI;
import org.phantomapi.Phantom;
import org.phantomapi.gui.Notification;
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.Priority;
import org.phantomapi.lang.Title;
import org.phantomapi.sfx.Audible;
import org.phantomapi.sfx.MFADistortion;
import org.phantomapi.sync.Task;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.util.D;
import org.phantomapi.util.F;
import org.phantomapi.util.M;
import org.phantomapi.vfx.ParticleEffect;

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
			
			try
			{
				BossBarAPI.removeAllBars(player);
				BossBarAPI.setMessage(player, C.LIGHT_PURPLE + "Level " + XP.getLevelForXp(getXp(player)), (float) ((float) XP.percentToNextLevel(getXp(player)) * 100.0), -1);
			}
			
			catch(Exception xe)
			{
				
			}
			
			if(levelNext > level)
			{
				Notification n = new Notification();
				Title t = new Title();
				t.setTitle("   ");
				t.setSubTitle(C.DARK_GRAY + "Level " + C.LIGHT_PURPLE + levelNext);
				t.setAction("   ");
				t.setFadeIn(0);
				t.setStayTime(0);
				t.setFadeOut(25);
				n.setTitle(t);
				n.setAudible(new GSound(Sound.WITHER_DEATH, 1f, 1.98f));
				n.setPriority(Priority.LOW);
				Phantom.queueNotification(player, n);
			}
		}
	}
	
	public static void playBeast(Player p)
	{
		float[] ix = new float[] {0};
		
		new Task(1)
		{
			@Override
			public void run()
			{
				Audible a = new GSound(Sound.FIREWORK_LARGE_BLAST);
				a.setPitch(0.1f + ix[0]);
				a.setVolume(ix[0]);
				a = new MFADistortion(12, 1.0f).distort(a);
				
				a.play(p);
				
				ix[0] += 0.04f;
				
				if(ix[0] >= 1.0f)
				{
					cancel();
					
					new TaskLater(2)
					{
						@Override
						public void run()
						{
							Audible a = new GSound(Sound.WITHER_DEATH);
							p.getWorld().strikeLightningEffect(p.getLocation());
							a.setPitch(0.7f);
							a = new MFADistortion(4, 1.8f).distort(a);
							
							a.play(p);
						}
					};
				}
			}
		};
	}
	
	public static void dropRandom(Location l)
	{
		if(M.r(0.001))
		{
			ParticleEffect.CLOUD.display(0.1f, 4, l, 32);
			ItemStack is = Icing.inst().getXp().createBoost((int) (12000 * Math.random()), 2.5 * Math.random());
			l.getWorld().dropItem(l, is);
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
