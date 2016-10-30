package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;

public abstract class TeleportCake extends SlicedCake implements TeleportSlice
{
	public TeleportCake(Controllable parentController, String codeName, String name, String description)
	{
		super(parentController, codeName, CakeType.TELEPORT, name, description);
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
