package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;

public abstract class KillCake extends SlicedCake implements KillSlice
{
	public KillCake(Controllable parentController, String codeName, CakeType type, String name, String description)
	{
		super(parentController, codeName, type, name, description);
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
