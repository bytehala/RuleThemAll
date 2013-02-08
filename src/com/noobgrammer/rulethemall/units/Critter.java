package com.noobgrammer.rulethemall.units;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Critter extends Unit
{
	private PhysicsWorld mPhysicsWorld;
	private Body mBody;
	private IEntityModifier mTrack;

	public Critter(float pX,
			float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			PhysicsWorld physicsWorld,
			FixtureDef fixtureDef
			)
	{
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
//		animate(200);
		mPhysicsWorld = physicsWorld;
		mBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, this, BodyType.DynamicBody, fixtureDef);
		mBody.setUserData(this);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, mBody, true, true));

	}
	
	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		mBody.setTransform((x + getWidth()/2)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (y + getHeight()/2)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, getRotation());
	}
	
	public void setPath(IEntityModifier track)
	{
		mTrack = track;
		registerEntityModifier(mTrack);
	}
	
	public void onAttacked()
	{
		unregisterEntityModifier(mTrack);
	}
	
	public void onMoveOn()
	{
		registerEntityModifier(mTrack);
	}
	
	public void update()
	{
		
	}

	@Override
	public void onClick(ClickDetector pClickDetector, int pPointerID,
			float pSceneX, float pSceneY) {
		// TODO Auto-generated method stub
		
	}

}
