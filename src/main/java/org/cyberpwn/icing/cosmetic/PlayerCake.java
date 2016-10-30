package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;

public abstract class PlayerCake extends SlicedCake implements PlayerSlice
{
	public PlayerCake(Controllable parentController, String codeName, CakeType type, String name, String description)
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
