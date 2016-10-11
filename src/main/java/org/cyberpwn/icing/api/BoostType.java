package org.cyberpwn.icing.api;

import org.phantomapi.clust.DataCluster;
import org.phantomapi.lang.GList;

public abstract class BoostType implements BoosterMesh
{
	protected DataCluster cc;
	protected final String name;
	protected int maxLevel;
	protected final GList<BoosterInstance> instances;
	
	public BoostType(String name, int maxLevel)
	{
		this.name = name;
		this.maxLevel = maxLevel;
		cc = new DataCluster();
		instances = new GList<BoosterInstance>();
	}
	
	@Override
	public int getMaxLevel()
	{
		return maxLevel;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public GList<BoosterInstance> getActiveBoosts()
	{
		return instances.copy();
	}
	
	@Override
	public double getEffectivePower()
	{
		double m = 0;
		
		for(BoosterInstance i : getActiveBoosts())
		{
			m += 0.25 * i.getLevel();
		}
		
		return m;
	}
	
	public void addBoost(BoosterInstance instance)
	{
		instances.add(instance);
	}
	
	public void removeBoost(BoosterInstance instance)
	{
		instances.remove(instance);
	}
	
	@Override
	public String getCodeName()
	{
		return "clust-hack";
	}
	
	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}
	
	@Override
	public void onNewConfig()
	{
		cc.set("boosts." + name.toLowerCase().replaceAll(" ", "-") + ".max-level", maxLevel);
	}
	
	@Override
	public void onReadConfig()
	{
		maxLevel = cc.getInt("boosts." + name.toLowerCase().replaceAll(" ", "-") + ".max-level");
	}
	
	@Override
	public abstract BoosterInstance createBooster(int level, String player);
}
