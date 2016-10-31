package org.cyberpwn.icing.cosmetic;

import org.bukkit.Material;
import org.phantomapi.util.C;
import org.phantomapi.world.MaterialBlock;

public enum CakeType
{
	PLAYER(C.AQUA + "Player FX", C.AQUA + "Ambient effects played near you.", new MaterialBlock(Material.ENCHANTED_BOOK)),
	ARROW(C.BLUE + "Arrow FX", C.BLUE + "Arrow effects played on the arrow.", new MaterialBlock(Material.ARROW)),
	KILL(C.GREEN + "Kill FX", C.GREEN + "Effects played when you kill someone.", new MaterialBlock(Material.IRON_SWORD)),
	DEATH(C.YELLOW + "Death FX", C.YELLOW + "Effects played when you are killed.", new MaterialBlock(Material.GOLD_CHESTPLATE)),
	TELEPORT(C.GOLD + "Teleport FX", C.GOLD + "Effects played when you teleport.", new MaterialBlock(Material.EYE_OF_ENDER));
	
	private String name;
	private String description;
	private MaterialBlock mb;
	
	private CakeType(String name, String description, MaterialBlock mb)
	{
		this.name = name;
		this.description = description;
		this.mb = mb;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public MaterialBlock getMb()
	{
		return mb;
	}
}
