package org.cyberpwn.icing.cakes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cosmetic.PlayerCake;
import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GList;
import org.phantomapi.physics.VectorMath;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.world.Area;
import org.phantomapi.world.MaterialBlock;

public class PlayerFrostWalk extends PlayerCake
{
	private GList<Block> process;
	
	public PlayerFrostWalk(Controllable parentController)
	{
		super(parentController, "player-frost-walk", "&dFrost Walk", "&5Everywhere you walk you trail snow.", new MaterialBlock(Material.SNOW_BALL));
		
		process = new GList<Block>();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onAmbient(Player p)
	{
		for(int j = 0; j < 12; j++)
		{
			Area a = new Area(p.getLocation(), 32);
			Location l = p.getLocation().clone().add((Math.random() * 2.0) - 1.0, (Math.random() * 2.0) - 1.0, (Math.random() * 2.0) - 1.0);
			l.add(VectorMath.reverse(p.getVelocity().clone().multiply(3)));
			
			new TaskLater(1)
			{
				@Override
				public void run()
				{
					if(!process.contains(l.getBlock()) && l.getBlock().getType().equals(Material.AIR) && l.getBlock().getRelative(BlockFace.DOWN).getType().isSolid() && !l.getBlock().getRelative(BlockFace.DOWN).getType().isTransparent())
					{
						if(l.getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("SLAB") || l.getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("PLATE") || l.getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("FENCE") || l.getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("STEP") || l.getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("STAIR"))
						{
							return;
						}
						
						process.add(l.getBlock());
						
						for(Player i : a.getNearbyPlayers())
						{
							i.sendBlockChange(l, Material.SNOW, (byte) ((int) Math.random() * 3));
						}
						
						new TaskLater(40)
						{
							@Override
							public void run()
							{
								for(Player i : a.getNearbyPlayers())
								{
									i.sendBlockChange(l, l.getBlock().getType(), l.getBlock().getData());
									
									process.remove(l.getBlock());
								}
							}
						};
					}
				}
			};
		}
	}
}
