package org.cyberpwn.icing.cakes;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.DeathCake;
import org.phantomapi.construct.Controllable;
import org.phantomapi.util.Explosion;
import org.phantomapi.world.MaterialBlock;

public class DeathExplode extends DeathCake
{
	public DeathExplode(Controllable parentController)
	{
		super(parentController, "death-explode", "&dExploding Death", "&5Explode when you are killed", new MaterialBlock(Material.TNT));
	}
	
	@Override
	public void onDeath(Player killed, LivingEntity killer)
	{
		new Explosion().power(0f).boom(killed.getLocation());
	}
}
