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

public class TeleportEnder extends TeleportCake
{
	public TeleportEnder(Controllable parentController)
	{
		super(parentController, "teleport-ender", "&dEnder Teleport", "&5Teleport like an enderman.", new MaterialBlock(Material.ENDER_PEARL));
	}
	
	@Override
	public void onTeleport(Player p)
	{
		ParticleEffect.PORTAL.display(1.5f, 32, p.getLocation().clone().add(0, 0.8, 0), 64);
		ParticleEffect.SPELL_WITCH.display(0.5f, 32, p.getLocation().clone().add(0, 0.8, 0), 64);
		new MFADistortion(4, 1.8f).distort(new GSound(Sound.ENTITY_ENDERMEN_TELEPORT, 1f, 0.4f)).play(p.getLocation());
		new GSound(Sound.ENTITY_WITHER_SPAWN, 1f, 0.8f).play(p.getLocation());
	}
}
