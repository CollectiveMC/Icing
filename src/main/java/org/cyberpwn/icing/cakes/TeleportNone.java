package org.cyberpwn.icing.cakes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.TeleportCake;
import org.phantomapi.construct.Controllable;
import org.phantomapi.world.MaterialBlock;

public class TeleportNone extends TeleportCake
{
	public TeleportNone(Controllable parentController)
	{
		super(parentController, "teleport-none", "&dNo Effect", "&5No Effect", new MaterialBlock(Material.BARRIER));
	}
	
	@Override
	public void onTeleport(Player p)
	{
		
	}
}
