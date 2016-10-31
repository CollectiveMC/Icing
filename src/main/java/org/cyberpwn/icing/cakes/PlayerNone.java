package org.cyberpwn.icing.cakes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.PlayerCake;
import org.phantomapi.construct.Controllable;
import org.phantomapi.world.MaterialBlock;

public class PlayerNone extends PlayerCake
{
	public PlayerNone(Controllable parentController)
	{
		super(parentController, "player-none", "&dNo Effect", "&5No Effect", new MaterialBlock(Material.BARRIER));
	}
	
	@Override
	public void onAmbient(Player p)
	{
		
	}
}
