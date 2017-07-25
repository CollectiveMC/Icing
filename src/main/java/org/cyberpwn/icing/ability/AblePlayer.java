package org.cyberpwn.icing.ability;

import org.bukkit.entity.Player;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.PD;
import org.phantomapi.clust.Tabled;
import org.phantomapi.lang.GList;

@Tabled("i.a")
public class AblePlayer extends ConfigurableObject
{
	private Player player;
	
	public AblePlayer(Player player)
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
		return PD.get(player).getConfiguration().crop("i.a");
	}
	
	public GList<String> getUnlockedAbilities()
	{
		GList<String> abilities = new GList<String>();
		
		for(String i : cc().keys())
		{
			abilities.add(i);
		}
		
		return abilities;
	}
	
	public void setAbilityLevel(String ability, long level)
	{
		PD.get(player).getConfiguration().set("i.a." + ability, level);
	}
	
	public void addAbilityLevel(String ability, long level)
	{
		setAbilityLevel(ability, level + getAbilityLevel(ability));
	}
	
	public long getAbilityLevel(String ability)
	{
		if(cc().contains(ability))
		{
			return cc().getLong(ability);
		}
		
		return 0;
	}
	
	public boolean isEnabled(String codeName)
	{
		if(!cc().contains(codeName + "-e"))
		{
			return true;
		}
		
		return cc().getBoolean(codeName + "-e");
	}
	
	public void setEnabled(String codeName, boolean e)
	{
		PD.get(player).getConfiguration().set("i.a." + codeName + "-e", e);
	}
}
