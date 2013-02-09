package com.noobgrammer.rulethemall.towers;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class BaseTower extends AnimatedSprite
{
	private PhysicsWorld mPhysicsWorld;
	private Body mBody;

	public BaseTower(float pX,
			float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			PhysicsWorld physicsWorld,
			FixtureDef fixtureDef
			)
	{
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		
		mPhysicsWorld = physicsWorld;
		mBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld, pX, pY, 150, BodyType.KinematicBody, fixtureDef, PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		mBody.setUserData(this);
//		PhysicsFactory.createCircleBody(this.mPhysicsWorld, this, BodyType.StaticBody, fixtureDef, PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, mBody, true, true));
	
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
	{
		return true;
	}
	

}
