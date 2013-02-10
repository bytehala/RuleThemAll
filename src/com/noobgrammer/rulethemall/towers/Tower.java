package com.noobgrammer.rulethemall.towers;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class Tower extends AnimatedSprite {

	protected boolean mIsMenuShown = false;

	public Tower(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
	}
	
	public abstract void showMenu();
	public abstract void hideMenu();
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
	{
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
		{
			if(mIsMenuShown)
				hideMenu();
			else
				showMenu();
		return true;
		}
		return false;
	}

}
