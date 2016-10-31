package org.cyberpwn.icing.cakes;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.KillCake;
import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GSound;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.vfx.ParticleEffect;
import org.phantomapi.world.MaterialBlock;

public class KillEZ extends KillCake
{
	public KillEZ(Controllable parentController)
	{
		super(parentController, "kill-ez", "&dEZ", "&5Plays a laugh sound when you kill a player", new MaterialBlock(Material.CAKE));
	}
	
	@Override
	public void onKilled(Player killer, LivingEntity killed)
	{
		if(killed instanceof Player)
		{
			new TaskLater(1)
			{
				@Override
				public void run()
				{
					new GSound(Sound.VILLAGER_YES, 1f, 0.7f).play(killer.getLocation());
				}
			};
			
			new TaskLater(7)
			{
				@Override
				public void run()
				{
					new GSound(Sound.VILLAGER_YES, 1f, 0.4f).play(killer.getLocation());
				}
			};
			
			ParticleEffect.VILLAGER_HAPPY.display(0.4f, 20, killer.getLocation().clone().add(0, 2, 0), 32);
		}
	}
}
