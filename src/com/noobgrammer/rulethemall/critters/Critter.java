package com.noobgrammer.rulethemall.critters;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Critter extends AnimatedSprite
{

	public Critter(float pX,
			float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		animate(200);
		// TODO Auto-generated constructor stub
	}
	
	public void update()
	{
		
	}

}
