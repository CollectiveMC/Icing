package org.cyberpwn.icing.cakes;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.KillCake;
import org.cyberpwn.icing.effect.Gravitorb;
import org.phantomapi.construct.Controllable;
import org.phantomapi.world.Area;
import org.phantomapi.world.MaterialBlock;

public class KillSoulEater extends KillCake
{
	public KillSoulEater(Controllable parentController)
	{
		super(parentController, "kill-soul-eater", "&dSoul Eater", "&5Absorbs the very essence of the dead.", new MaterialBlock(Material.NETHER_STAR));
	}
	
	@Override
	public void onKilled(Player killer, LivingEntity killed)
	{
		Area a = new Area(killed.getLocation().clone().add(0, 4, 0), 4.5);
		new Gravitorb(a.random(), killer);
		new Gravitorb(a.random(), killer);
		new Gravitorb(a.random(), killer);
	}
}
