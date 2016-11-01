package org.cyberpwn.icing.cosmetic;

import org.bukkit.entity.Player;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.Keyed;
import org.phantomapi.clust.Tabled;
import org.phantomapi.lang.GList;

@Tabled("ice_cake_players")
public class CakePlayerData extends ConfigurableObject
{
	private Player player;
	
	@Keyed("owned")
	public GList<String> owned = new GList<String>();
	
	@Keyed("equipped")
	public GList<String> equipped = new GList<String>();
	
	public CakePlayerData(Player player)
	{
		super(player.getUniqueId().toString());
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public GList<String> getOwned()
	{
		return owned;
	}
	
	public GList<String> getEquipped()
	{
		return equipped;
	}
}
