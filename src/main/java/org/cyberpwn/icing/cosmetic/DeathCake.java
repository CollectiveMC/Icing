package org.cyberpwn.icing.cosmetic;

import org.phantomapi.construct.Controllable;
import org.phantomapi.world.MaterialBlock;

public abstract class DeathCake extends SlicedCake implements DeathSlice
{
	public DeathCake(Controllable parentController, String codeName, String name, String description, MaterialBlock mb)
	{
		super(parentController, codeName, CakeType.DEATH, name, description, mb);
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
