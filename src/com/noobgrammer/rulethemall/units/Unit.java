package com.noobgrammer.rulethemall.units;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.noobgrammer.rulethemall.states.IState;


public abstract class Unit extends AnimatedSprite implements IClickDetectorListener{
	
	

	public Unit(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
	}

	public IState mCurrentState;
	protected ClickDetector mClickDetector;
	

}
