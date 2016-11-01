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
}
