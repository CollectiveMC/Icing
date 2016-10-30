package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;

public abstract class PlayerCake extends SlicedCake implements PlayerSlice
{
	public PlayerCake(Controllable parentController, String codeName, String name, String description)
	{
		super(parentController, codeName, CakeType.PLAYER, name, description);
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
