package org.cyberpwn.icing.cosmetic;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface KillSlice
{
	public void onKilled(Player killer, LivingEntity killed);
}
