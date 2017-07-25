package org.cyberpwn.icing.cakes;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.TeleportCake;
import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GSound;
import org.phantomapi.sfx.MFADistortion;
import org.phantomapi.vfx.ParticleEffect;
import org.phantomapi.world.MaterialBlock;

public class TeleportPhantom extends TeleportCake
{
	public TeleportPhantom(Controllable parentController)
	{
		super(parentController, "teleport-phantom", "&dPhantom Teleport", "&5Teleport like a phantom.", new MaterialBlock(Material.CAULDRON_ITEM));
	}
	
	@Override
	public void onTeleport(Player p)
	{
		ParticleEffect.SMOKE_NORMAL.display(0.1f, 32, p.getLocation().clone().add(0, 0.8, 0), 64);
		ParticleEffect.LAVA.display(0.1f, 32, p.getLocation().clone().add(0, 0.8, 0), 64);
		new MFADistortion(4, 1.8f).distort(new GSound(Sound.ENTITY_BAT_TAKEOFF, 1f, 0.4f)).play(p.getLocation());
		new GSound(Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.8f).play(p.getLocation());
	}
}
