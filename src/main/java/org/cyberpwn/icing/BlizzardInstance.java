package org.cyberpwn.icing;

import org.phantomapi.clust.Configurable;

public interface BlizzardInstance extends Configurable
{
	public void onStart();
	
	public void tick();
	
	public void onExpire();
	
	public boolean isExpired();
	
	public String getSource();
	
	public double getMultiplier();
	
	public int minutesLeft();
	
	public void onTick();
	
	public BlizzardInstance copy();
}
