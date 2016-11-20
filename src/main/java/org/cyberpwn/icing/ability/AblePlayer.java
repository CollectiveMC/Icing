package org.cyberpwn.icing.ability;

import org.bukkit.entity.Player;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.Redis;
import org.phantomapi.lang.GList;

@Redis("i.a")
public class AblePlayer extends ConfigurableObject
{
	private Player player;
	
	public AblePlayer(Player player)
	{
		super(player.getUniqueId().toString());
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public GList<String> getUnlockedAbilities()
	{
		GList<String> abilities = new GList<String>();
		
		for(String i : getConfiguration().keys())
		{
			abilities.add(i);
		}
		
		return abilities;
	}
	
	public void setAbilityLevel(String ability, long level)
	{
		getConfiguration().set(ability, level);
	}
	
	public void addAbilityLevel(String ability, long level)
	{
		setAbilityLevel(ability, level + getAbilityLevel(ability));
	}
	
	public long getAbilityLevel(String ability)
	{
		if(getConfiguration().contains(ability))
		{
			return getConfiguration().getLong(ability);
		}
		
		return 0;
	}
	
	public boolean isEnabled(String codeName)
	{
		if(!getConfiguration().contains(codeName + "-e"))
		{
			return true;
		}
		
		return getConfiguration().getBoolean(codeName + "-e");
	}
	
	public void setEnabled(String codeName, boolean e)
	{
		getConfiguration().set(codeName + "-e", e);
	}
}
