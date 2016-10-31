package org.cyberpwn.icing.cakes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.ArrowCake;
import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GSound;
import org.phantomapi.nms.NMSX;
import org.phantomapi.physics.VectorMath;
import org.phantomapi.vfx.ParticleEffect;
import org.phantomapi.world.MaterialBlock;

public class ArrowFirework extends ArrowCake
{
	public ArrowFirework(Controllable parentController)
	{
		super(parentController, "arrow-firework", "&dArrow Firework", "&5Shoots arrows as fireworks, blowing up when they hit", new MaterialBlock(Material.FIREWORK));
	}
	
	@Override
	public void onAmbient(Arrow a, Player shooter)
	{
		if(a.isCritical())
		{
			ParticleEffect.FIREWORKS_SPARK.display(VectorMath.reverse(a.getVelocity()), 0.2f, a.getLocation(), 128);
		}
	}
	
	@Override
	public void onHitEntity(Arrow a, Player p, LivingEntity e)
	{
		if(a.isCritical())
		{
			NMSX.launchFirework(a.getLocation());
		}
	}
	
	@Override
	public void onHit(Arrow a, Player shooter, Location land)
	{
		
	}
	
	@Override
	public void onShoot(Arrow a, Player shooter)
	{
		if(a.isCritical())
		{
			new GSound(Sound.FIREWORK_LAUNCH, 1f, 1.5f).play(shooter.getLocation());
		}
	}
}
