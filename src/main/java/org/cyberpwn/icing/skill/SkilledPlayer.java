package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.PD;
import org.phantomapi.clust.Redis;
import org.phantomapi.lang.GList;

@Redis("i.s")
public class SkilledPlayer extends ConfigurableObject
{
	private Player player;
	
	public SkilledPlayer(Player player)
	{
		super(player.getUniqueId().toString());
		
		this.player = player;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public DataCluster cc()
	{
		return PD.get(player).getConfiguration().crop("i.s");
	}
	
	public GList<String> getKnownSkills()
	{
		GList<String> skills = new GList<String>();
		
		for(String i : cc().keys())
		{
			skills.add(i);
		}
		
		return skills;
	}
	
	public void setSkill(String skill, long xp)
	{
		PD.get(player).getConfiguration().set("i.s." + skill, xp);
	}
	
	public void addSkill(String skill, long xp)
	{
		setSkill(skill, xp + getSkill(skill));
	}
	
	public void setEnabled(String skill, boolean enabled)
	{
		getConfiguration().set(skill + "-e", enabled);
	}
	
	public boolean isEnabled(String skill)
	{
		if(!cc().contains(skill + "-e"))
		{
			return true;
		}
		
		return cc().getBoolean(skill + "-e");
	}
	
	public long getSkill(String skill)
	{
		if(cc().contains(skill))
		{
			return cc().getLong(skill);
		}
		
		return 0;
	}
	
	public void setSkillPoints(String skill, long amt)
	{
		PD.get(player).getConfiguration().set("i.s." + skill + "-p", amt);
	}
	
	public void addSkillPoints(String skill, long amt)
	{
		setSkillPoints(skill, amt + getSkillPoints(skill));
	}
	
	public void takeSkillPoints(String skill, long amt)
	{
		setSkillPoints(skill, getSkillPoints(skill) - amt);
	}
	
	public long getSkillPoints(String skill)
	{
		if(cc().contains(skill + "-p"))
		{
			return cc().getLong(skill + "-p");
		}
		
		return 0;
	}
	
	public void setSkillBuff(String skill, long amt)
	{
		PD.get(player).getConfiguration().set("i.s." + skill + "-b", amt);
	}
	
	public void addSkillBuff(String skill, long amt)
	{
		setSkillBuff(skill, amt + getSkillBuff(skill));
	}
	
	public void takeSkillBuff(String skill, long amt)
	{
		setSkillBuff(skill, getSkillBuff(skill) - amt);
	}
	
	public long getSkillBuff(String skill)
	{
		if(cc().contains(skill + "-b"))
		{
			return cc().getLong(skill + "-b");
		}
		
		return 0;
	}
}
