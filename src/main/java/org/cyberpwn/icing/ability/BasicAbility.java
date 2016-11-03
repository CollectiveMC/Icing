package org.cyberpwn.icing.ability;

import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;

public class BasicAbility extends ConfigurableController implements Ability
{
	@Comment("The level a player's skill and level must be at to unlock this ability.")
	@Keyed("ability.level")
	public int level = 1;
	
	@Comment("The level increase from the initial or last upgrade to upgrade again.")
	@Keyed("ability.step")
	public int levelStep = 2;
	
	@Comment("The Cost in skill specific shards to unlock this ability")
	@Keyed("ability.cost")
	public int unlockCost = 1;
	
	@Comment("The Cost in skill specific shards to upgrade this ability")
	@Keyed("ability.cost")
	public int upgradeCost = 2;
	
	public BasicAbility(Controllable parentController, String codeName)
	{
		super(parentController, codeName);
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
