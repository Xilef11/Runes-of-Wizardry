package com.zpig333.runesofwizardry.client.render;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

public class RenderDustActive extends RenderDustPlaced {

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.client.render.RenderDustPlaced#renderTileEntityAt(com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced, double, double, double, float, int)
	 */
	@Override
	public void renderTileEntityAt(TileEntityDustPlaced tileEntity,	double relativeX, double relativeY, double relativeZ,float partialTicks, int blockDamageProgress) {
		super.renderTileEntityAt(tileEntity, relativeX, relativeY, relativeZ,partialTicks, blockDamageProgress);
		//TODO render star and beam if necessary
	}
	
}
