package org.cyberpwn.icing.api;

public interface BoosterMesh
{
	public int getMaxLevel();
	
	public String getName();
	
	public BoosterInstance createBooster(int level, String player);
}
