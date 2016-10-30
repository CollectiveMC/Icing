package org.cyberpwn.icing;

import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;

public abstract class SlicedCake extends ConfigurableController implements Cake
{
	@Comment("The display name")
	@Keyed("cake.name")
	public String name = "&aCake";
	
	@Comment("The description")
	@Keyed("cake.description")
	public String description = "&cSliced Cake";
	
	private CakeType type;
	
	public SlicedCake(Controllable parentController, String codeName, CakeType type, String name, String description)
	{
		super(parentController, codeName);
		
		this.name = name;
		this.description = description;
		this.type = type;
	}
	
	@Override
	public CakeType getType()
	{
		return type;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}
}
