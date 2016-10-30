package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;

public abstract class ArrowCake extends SlicedCake implements ArrowSlice
{
	public ArrowCake(Controllable parentController, String codeName, String name, String description)
	{
		super(parentController, codeName, CakeType.ARROW, name, description);
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
