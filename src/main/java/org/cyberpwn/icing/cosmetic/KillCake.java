package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;
import org.phantomapi.world.MaterialBlock;

public abstract class KillCake extends SlicedCake implements KillSlice
{
	public KillCake(Controllable parentController, String codeName, String name, String description, MaterialBlock mb)
	{
		super(parentController, codeName, CakeType.KILL, name, description, mb);
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
