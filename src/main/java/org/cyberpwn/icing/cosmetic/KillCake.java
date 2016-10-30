package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;

public abstract class KillCake extends SlicedCake implements KillSlice
{
	public KillCake(Controllable parentController, String codeName, String name, String description)
	{
		super(parentController, codeName, CakeType.KILL, name, description);
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
