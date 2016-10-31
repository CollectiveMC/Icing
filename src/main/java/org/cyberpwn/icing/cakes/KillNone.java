package org.cyberpwn.icing.cakes;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.KillCake;
import org.phantomapi.construct.Controllable;
import org.phantomapi.world.MaterialBlock;

public class KillNone extends KillCake
{
	public KillNone(Controllable parentController)
	{
		super(parentController, "kill-none", "&dNo Effect", "&5No Effect", new MaterialBlock(Material.BARRIER));
	}
	
	@Override
	public void onKilled(Player killer, LivingEntity killed)
	{
		
	}
}
