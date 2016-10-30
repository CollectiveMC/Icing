package org.cyberpwn.icing.cosmetic;

import org.phantomapi.world.MaterialBlock;

public interface Cake
{
	public CakeType getType();
	
	public String getName();
	
	public String getDescription();
	
	public MaterialBlock getMaterial();
}
