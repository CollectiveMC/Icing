package org.cyberpwn.icing.cosmetic;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface DeathSlice
{
	public void onDeath(Player killed, LivingEntity killer);
}
