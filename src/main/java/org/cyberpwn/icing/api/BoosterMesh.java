package org.cyberpwn.icing.api;

import org.phantomapi.lang.GList;

public interface BoosterMesh
{
	public int getMaxLevel();
	
	public String getName();
	
	public BoosterInstance createBooster(int level, String player);
	
	public GList<BoosterInstance> getActiveBoosts();
	
	public double getEffectivePower();
}
