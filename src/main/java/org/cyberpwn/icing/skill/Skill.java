package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;
import org.cyberpwn.icing.ability.Ability;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.MaterialBlock;

public interface Skill
{
	public long getXp(Player p);
	
	public void addXp(Player p, long amt);
	
	public long getLevel(Player p);
	
	public long getShards(Player p);
	
	public void addShards(Player p, long amt);
	
	public void takeShards(Player p, long amt);
	
	public void setShards(Player p, long amt);
	
	public double getProgress(Player p);
	
	public double getBufferPercent(Player p);
	
	public long getXpLeft(Player p);
	
	public String fancyName();
	
	public void popRewardMap();
	
	public void addReward(Player p, Integer r);
	
	public MaterialBlock getSkillMaterial();
	
	public double getPercentToShards(Player p, int needed);
	
	public double getPercentToLevel(Player p, int needed);
	
	public long getBuffer(Player player);
	
	public void setBuffer(Player player, long skill);
	
	public void addBuffer(Player player, long skill);
	
	public void takeBuffer(Player player, long skill);
	
	public String name();
	
	public GMap<Player, Integer> getRewardCache();
	
	public XPReason getReason();
	
	public GList<Ability> getAbilities();
}
