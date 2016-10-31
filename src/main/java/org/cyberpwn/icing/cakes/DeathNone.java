package org.cyberpwn.icing.cakes;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.DeathCake;
import org.phantomapi.construct.Controllable;
import org.phantomapi.world.MaterialBlock;

public class DeathNone extends DeathCake
{
	public DeathNone(Controllable parentController)
	{
		super(parentController, "death-none", "&dNo Effect", "&5No Effect", new MaterialBlock(Material.BARRIER));
	}
	
	@Override
	public void onDeath(Player killed, LivingEntity killer)
	{
		
	}
}
