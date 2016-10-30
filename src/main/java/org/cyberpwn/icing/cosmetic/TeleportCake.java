package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;

public abstract class TeleportCake extends SlicedCake implements TeleportSlice
{
	public TeleportCake(Controllable parentController, String codeName, CakeType type, String name, String description)
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
