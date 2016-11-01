package org.cyberpwn.icing.effect;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.phantomapi.physics.VectorMath;
import org.phantomapi.sync.Task;
import org.phantomapi.vfx.ParticleEffect;
import org.phantomapi.vfx.PhantomEffect;
import org.phantomapi.vfx.VisualEffect;

public class Gravitorb
{
	private VisualEffect effect;
	private Vector velocity;
	private Location location;
	
	public Gravitorb(Location l, Entity target)
	{
		effect = new PhantomEffect()
		{
			@Override
			public void play(Location l)
			{
				ParticleEffect.FLAME.display(velocity, 0.4f, location, 64);
			}
		};
		
		velocity = Vector.getRandom().multiply(0.4);
		location = l;
		
		new Task(0)
		{
			@Override
			public void run()
			{
				if(target.getLocation().distanceSquared(location) <= 4)
				{
					ParticleEffect.CLOUD.display(0.1f, 12, location, 32);
					cancel();
					return;
				}
				
				Vector m = VectorMath.direction(location, target.getLocation());
				velocity.add(m.clone().multiply(0.01));
				location.add(m);
				effect.play(location);
			}
		};
	}
}
