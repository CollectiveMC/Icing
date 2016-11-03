package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.Tabled;
import org.phantomapi.lang.GList;

@Tabled("ice_skill_player")
public class SkilledPlayer extends ConfigurableObject
{
	private Player player;
	
	public SkilledPlayer(Player player)
	{
		super(player.getUniqueId().toString());
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public GList<String> getKnownSkills()
	{
		GList<String> skills = new GList<String>();
		
		for(String i : getConfiguration().keys())
		{
			skills.add(i);
		}
		
		return skills;
	}
	
	public void setSkill(String skill, long xp)
	{
		getConfiguration().set(skill, xp);
	}
	
	public void addSkill(String skill, long xp)
	{
		setSkill(skill, xp + getSkill(skill));
	}
	
	public long getSkill(String skill)
	{
		if(getConfiguration().contains(skill))
		{
			return getConfiguration().getLong(skill);
		}
		
		return 0;
	}
	
	public void setSkillPoints(String skill, long amt)
	{
		getConfiguration().set(skill + "-p", amt);
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
		if(getConfiguration().contains(skill + "-p"))
		{
			return getConfiguration().getLong(skill + "-p");
		}
		
		return 0;
	}
	
	public void setSkillBuff(String skill, long amt)
	{
		getConfiguration().set(skill + "-b", amt);
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
		if(getConfiguration().contains(skill + "-b"))
		{
			return getConfiguration().getLong(skill + "-b");
		}
		
		return 0;
	}
}
