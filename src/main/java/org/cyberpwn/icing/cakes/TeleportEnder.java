package org.cyberpwn.icing.cakes;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.TeleportCake;
import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GSound;
import org.phantomapi.sfx.MFADistortion;
import org.phantomapi.vfx.ParticleEffect;

public class TeleportEnder extends TeleportCake
{
	public TeleportEnder(Controllable parentController)
	{
		super(parentController, "teleport-ender", type, name, description);
	}
	
	@Override
	public void onTeleport(Player p)
	{
		ParticleEffect.PORTAL.display(0.5f, 12, p.getLocation().clone().add(0, 0.8, 0), 64);
		new MFADistortion(4, 1.8f).distort(new GSound(Sound.ENDERMAN_TELEPORT, 1f, 0.4f)).play(p.getLocation());
	}
}
