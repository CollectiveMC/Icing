package org.cyberpwn.icing.cakes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.ArrowCake;
import org.phantomapi.construct.Controllable;
import org.phantomapi.world.MaterialBlock;

public class ArrowNone extends ArrowCake
{
	public ArrowNone(Controllable parentController)
	{
		super(parentController, "arrow-none", "&dNo Effect", "&5No Effect", new MaterialBlock(Material.BARRIER));
	}
	
	@Override
	public void onAmbient(Arrow a, Player shooter)
	{
		
	}
	
	@Override
	public void onHit(Arrow a, Player shooter, Location land)
	{
		
	}
	
	@Override
	public void onShoot(Arrow a, Player shooter)
	{
		
	}
	
	@Override
	public void onHitEntity(Arrow a, Player p, LivingEntity e)
	{
		
	}
}
