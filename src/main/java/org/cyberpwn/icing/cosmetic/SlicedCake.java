package org.cyberpwn.icing.cosmetic;

import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.W;

public abstract class SlicedCake extends ConfigurableController implements Cake
{
	@Comment("The display name")
	@Keyed("cake.name")
	public String name = "&aCake";
	
	@Comment("The description")
	@Keyed("cake.description")
	public String description = "&cSliced Cake";
	
	@Comment("The material")
	@Keyed("cake.material")
	public String material = "STICK";
	
	private CakeType type;
	
	public SlicedCake(Controllable parentController, String codeName, CakeType type, String name, String description, MaterialBlock mb)
	{
		super(parentController, codeName);
		
		this.name = name;
		this.description = description;
		this.type = type;
		material = mb.toString();
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
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public MaterialBlock getMaterial()
	{
		return W.getMaterialBlock(material);
	}
}
