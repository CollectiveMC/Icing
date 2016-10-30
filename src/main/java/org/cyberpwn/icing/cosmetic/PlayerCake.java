package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;
import org.phantomapi.world.MaterialBlock;

public abstract class PlayerCake extends SlicedCake implements PlayerSlice
{
	public PlayerCake(Controllable parentController, String codeName, String name, String description, MaterialBlock mb)
	{
		super(parentController, codeName, CakeType.PLAYER, name, description, mb);
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
