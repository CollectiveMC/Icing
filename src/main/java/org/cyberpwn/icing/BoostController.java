package org.cyberpwn.icing;

import org.cyberpwn.icing.api.BoosterMesh;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GList;

public class BoostController extends ConfigurableController
{
	private GList<BoosterMesh> meshes;
	
	public BoostController(Controllable parentController)
	{
		super(parentController, "boosters");
		
		meshes = new GList<BoosterMesh>();
	}
	
	public void registerMesh(BoosterMesh mesh)
	{
		meshes.add(mesh);
	}
	
	@Override
	public void onStart()
	{
		// TODO create meshes and register them
		
		for(BoosterMesh i : meshes)
		{
			i.onNewConfig();
			getConfiguration().add(i.getConfiguration());
		}
		
		loadCluster(this);
		
		for(BoosterMesh i : meshes)
		{
			i.getConfiguration().clear();
			i.getConfiguration().setData(getConfiguration().getData());
			i.onReadConfig();
		}
	}
	
	@Override
	public void onStop()
	{
		
	}
}
