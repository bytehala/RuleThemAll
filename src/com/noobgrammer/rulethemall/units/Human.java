package com.noobgrammer.rulethemall.units;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Human extends Unit{
	
	private PhysicsWorld mPhysicsWorld;
	private Body mBody;

	public Human(float pX,
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
		mBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld, pX, pY, 30, BodyType.KinematicBody, fixtureDef, PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		mBody.setUserData(this);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, mBody, true, true));
		
		this.mClickDetector = new ClickDetector(this);

	}
	
	public void acquireTarget(Critter target)
	{
		target.onAttacked();
		target.onMoveOn();
		
	}

	@Override
	public void onClick(ClickDetector pClickDetector, int pPointerID,
			float pSceneX, float pSceneY) {
		Log.d("LEM!", "I was clicked");
		
	}


}
