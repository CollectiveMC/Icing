package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;
import org.phantomapi.world.MaterialBlock;

public abstract class TeleportCake extends SlicedCake implements TeleportSlice
{
	public TeleportCake(Controllable parentController, String codeName, String name, String description, MaterialBlock mb)
	{
		super(parentController, codeName, CakeType.TELEPORT, name, description, mb);
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
