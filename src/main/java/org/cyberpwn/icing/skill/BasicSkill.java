package org.cyberpwn.icing.skill;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.Icing;
import org.cyberpwn.icing.ability.Ability;
import org.cyberpwn.icing.xp.SkillCurrency;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.PD;
import org.phantomapi.construct.Controllable;
import org.phantomapi.currency.Transaction;
import org.phantomapi.gui.Notification;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.Priority;
import org.phantomapi.lang.Title;
import org.phantomapi.text.RTX;
import org.phantomapi.text.SYM;
import org.phantomapi.util.C;

public abstract class BasicSkill extends ConfigurableController implements Skill
{
	private GMap<Player, Integer> rewardCache;
	private XPReason reason;
	private GList<Ability> abilities;
	
	public BasicSkill(Controllable parentController, String codeName, XPReason reason)
	{
		super(parentController, codeName);
		
		abilities = new GList<Ability>();
		rewardCache = new GMap<Player, Integer>();
		this.reason = reason;
		createControllers();
		
		for(Controllable i : getControllers())
		{
			if(i instanceof Ability)
			{
				abilities.add((Ability) i);
			}
			
			if(i instanceof Configurable)
			{
				loadCluster((Configurable) i, "abilities");
			}
		}
	}
	
	public abstract void createControllers();
	
	@Override
	public long getXp(Player p)
	{
		return Icing.inst().getSk().getSkillDataController().get(p).getSkill(getCodeName());
	}
	
	@Override
	public void addXp(Player p, long aam)
	{
		if(XP.getBoost(p) < -1)
		{
			return;
		}
		
		aam = (long) (aam - (aam * PD.get(p).getConfiguration().getDouble("i.x.d")));
		long amt = (long) (aam + (aam * XP.getBoost(p)));
		
		if(amt <= 0)
		{
			return;
		}
		
		long level = XP.getLevelForXp(getXp(p));
		Icing.inst().getSk().getSkillDataController().get(p).addSkill(getCodeName(), amt);
		XP.giveXp(p, aam, reason);
		addBuffer(p, amt);
		long nextLevel = XP.getLevelForXp(getXp(p));
		int m = 0;
		
		while(getBuffer(p) >= 4096)
		{
			takeBuffer(p, 4096);
			new Transaction(new SkillCurrency(getCodeName())).to(p).amount(1.0).noDiff().commit();
			m++;
		}
		
		if(m > 0)
		{
			Notification n = new Notification();
			Title t = new Title();
			t.setTitle("   ");
			t.setSubTitle(C.DARK_GRAY + "+ " + C.AQUA + m + " " + fancyName() + " Shard");
			t.setAction(C.AQUA + fancyName() + " Shard Earned!");
			t.setFadeIn(0);
			t.setStayTime(0);
			t.setFadeOut(25);
			n.setTitle(t);
			n.setAudible(new GSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 0.38f));
			n.setPriority(Priority.LOW);
			XP.q(p, n);
			XP.giveXp(p, nextLevel * 30, XPReason.SKILL_PROGRESSION);
		}
		
		if(nextLevel > level)
		{
			Notification n = new Notification();
			Title t = new Title();
			t.setTitle("   ");
			t.setSubTitle(C.DARK_GRAY + fancyName() + " " + C.LIGHT_PURPLE + nextLevel);
			t.setAction(C.LIGHT_PURPLE + fancyName() + " Leveled Up!");
			t.setFadeIn(0);
			t.setStayTime(0);
			t.setFadeOut(20);
			n.setTitle(t);
			n.setAudible(new GSound(Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.98f));
			n.setPriority(Priority.LOW);
			XP.q(p, n);
			XP.giveXp(p, nextLevel * 30, XPReason.SKILL_PROGRESSION);
			
			RTX rtx = new RTX();
			rtx.addText("" + SYM.SYMBOL_SNOWFLAKE, C.GRAY);
			rtx.addText(" " + fancyName() + " " + nextLevel, C.GREEN);
			rtx.addTextFireCommand(" (click)", "/sk show " + getCodeName(), C.GRAY);
			rtx.tellRawTo(p);
		}
	}
	
	@Override
	public double getBufferPercent(Player p)
	{
		return (double) getBuffer(p) / 4096.0;
	}
	
	@Override
	public long getBuffer(Player player)
	{
		return Icing.inst().getSk().getSkillDataController().get(player).getSkillBuff(getCodeName());
	}
	
	@Override
	public void setBuffer(Player player, long skill)
	{
		Icing.inst().getSk().getSkillDataController().get(player).setSkillBuff(getCodeName(), skill);
	}
	
	@Override
	public void addBuffer(Player player, long skill)
	{
		Icing.inst().getSk().getSkillDataController().get(player).addSkillBuff(getCodeName(), skill);
	}
	
	@Override
	public void takeBuffer(Player player, long skill)
	{
		Icing.inst().getSk().getSkillDataController().get(player).takeSkillBuff(getCodeName(), skill);
	}
	
	@Override
	public String name()
	{
		return getCodeName();
	}
	
	@Override
	public long getLevel(Player p)
	{
		return XP.getLevelForXp(getXp(p));
	}
	
	@Override
	public long getShards(Player p)
	{
		return Icing.getInst().getSk().getSkillDataController().get(p).getSkillPoints(getCodeName());
	}
	
	@Override
	public double getProgress(Player p)
	{
		return XP.percentToNextLevel(getXp(p));
	}
	
	@Override
	public double getPercentToShards(Player p, int needed)
	{
		double has = (getShards(p) * 4096) + (getBuffer(p));
		double needs = 4096.0 * needed;
		return has / needs;
	}
	
	@Override
	public double getPercentToLevel(Player p, int needed)
	{
		double has = getXp(p);
		double needs = XP.getXpForLevel(needed);
		return has / needs;
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
	
	@Override
	public void addReward(Player p, Integer r)
	{
		if(p == null)
		{
			return;
		}
		
		if(r <= 0)
		{
			return;
		}
		
		if(!rewardCache.containsKey(p))
		{
			rewardCache.put(p, 0);
		}
		
		rewardCache.put(p, rewardCache.get(p) + r);
	}
	
	@Override
	public void popRewardMap()
	{
		for(Player i : rewardCache.k())
		{
			if(i == null)
			{
				continue;
			}
			
			if(rewardCache.get(i) <= 0)
			{
				continue;
			}
			
			addXp(i, rewardCache.get(i));
		}
		
		rewardCache.clear();
	}
	
	@Override
	public GMap<Player, Integer> getRewardCache()
	{
		return rewardCache;
	}
	
	@Override
	public XPReason getReason()
	{
		return reason;
	}
	
	@Override
	public GList<Ability> getAbilities()
	{
		return abilities;
	}
	
	@Override
	public void addShards(Player p, long amt)
	{
		Icing.getInst().getSk().getSkillDataController().get(p).addSkillPoints(getCodeName(), amt);
	}
	
	@Override
	public void takeShards(Player p, long amt)
	{
		Icing.getInst().getSk().getSkillDataController().get(p).takeSkillPoints(getCodeName(), amt);
	}
	
	@Override
	public void setShards(Player p, long amt)
	{
		Icing.getInst().getSk().getSkillDataController().get(p).setSkillPoints(getCodeName(), amt);
	}
}
