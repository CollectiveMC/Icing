package org.cyberpwn.icing.cosmetic;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface ArrowSlice
{
	public void onAmbient(Arrow a, Player shooter);
	
	public void onHit(Arrow a, Player shooter, Location land);
	
	public void onShoot(Arrow a, Player shooter);
	
	public void onHitEntity(Arrow a, Player p, LivingEntity e);
}
