package org.cyberpwn.icing.skill;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.Icing;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.Phantom;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.construct.Controllable;
import org.phantomapi.gui.Notification;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.Priority;
import org.phantomapi.lang.Title;
import org.phantomapi.util.C;

public abstract class BasicSkill extends ConfigurableController implements Skill
{
	private GMap<Player, Integer> rewardCache;
	private XPReason reason;
	
	public BasicSkill(Controllable parentController, String codeName, XPReason reason)
	{
		super(parentController, codeName);
		
		rewardCache = new GMap<Player, Integer>();
		this.reason = reason;
	}
	
	@Override
	public long getXp(Player p)
	{
		return Icing.inst().getSk().getSkillDataController().get(p).getSkill(getCodeName());
	}
	
	@Override
	public void addXp(Player p, long amt)
	{
		long level = XP.getLevelForXp(getXp(p));
		Icing.inst().getSk().getSkillDataController().get(p).addSkill(getCodeName(), amt);
		XP.giveXp(p, amt, reason);
		long nextLevel = XP.getLevelForXp(getXp(p));
		
		if(nextLevel > level)
		{
			Notification n = new Notification();
			Title t = new Title();
			t.setTitle("   ");
			t.setSubTitle(C.DARK_GRAY + fancyName() + " " + C.LIGHT_PURPLE + nextLevel);
			t.setAction(C.LIGHT_PURPLE + fancyName() + " Leveled Up!");
			t.setFadeIn(5);
			t.setStayTime(15);
			t.setFadeOut(20);
			n.setTitle(t);
			n.setAudible(new GSound(Sound.LEVEL_UP, 1f, 1.98f));
			n.setPriority(Priority.LOW);
			Phantom.queueNotification(p, n);
		}
	}
	
	@Override
	public long getLevel(Player p)
	{
		return XP.getLevelForXp(getXp(p));
	}
	
	@Override
	public double getProgress(Player p)
	{
		return XP.percentToNextLevel(getXp(p));
	}
	
	@Override
	public long getXpLeft(Player p)
	{
		return (long) XP.xpToNextLevel(getXp(p));
	}
	
	@Override
	public String fancyName()
	{
		return StringUtils.capitalize(getCodeName());
	}
	
	public void addReward(Player p, Integer r)
	{
		if(p == null)
		{
			return;
		}
		
		if(!rewardCache.containsKey(p))
		{
			rewardCache.put(p, 0);
		}
		
		rewardCache.put(p, rewardCache.get(p) + r);
	}
	
	public void popRewardMap()
	{
		for(Player i : rewardCache.k())
		{
			addXp(i, rewardCache.get(i));
		}
		
		rewardCache.clear();
	}
}
