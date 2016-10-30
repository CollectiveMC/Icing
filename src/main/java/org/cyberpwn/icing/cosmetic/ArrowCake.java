package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;
import org.phantomapi.world.MaterialBlock;

public abstract class ArrowCake extends SlicedCake implements ArrowSlice
{
	public ArrowCake(Controllable parentController, String codeName, String name, String description, MaterialBlock mb)
	{
		super(parentController, codeName, CakeType.ARROW, name, description, mb);
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
}
