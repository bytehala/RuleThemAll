package com.noobgrammer.rulethemall.towers;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class BaseTower extends Tower {
	private PhysicsWorld mPhysicsWorld;
	private Body mBody;
	private Scene mScene;

	private TowerTypes[] upGrades;
	private TextureRegion[] txRegions;
	private Sprite one;
	private Sprite two;
	private Sprite three;
	private Sprite four;

	public BaseTower(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			PhysicsWorld physicsWorld, FixtureDef fixtureDef) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

		mPhysicsWorld = physicsWorld;
		mBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld, pX, pY,
				150, BodyType.KinematicBody, fixtureDef,
				PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		mBody.setUserData(this);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				mBody, true, true));

		upGrades = new TowerTypes[4];
		txRegions = new TextureRegion[4];

	}

	public void setUpgradeTowerTypes(TowerTypes upOne, TowerTypes upTwo,
			TowerTypes upThree, TowerTypes upFour) {
		upGrades[0] = upOne;
		upGrades[1] = upTwo;
		upGrades[2] = upThree;
		upGrades[3] = upFour;
	}

	public void setScene(Scene scene) {
		mScene = scene;
	}

	public void setPortraitTextureRegions(TextureRegion txOne,
			TextureRegion txTwo, TextureRegion txThree, TextureRegion txFour) {
		txRegions[0] = txOne;
		txRegions[1] = txTwo;
		txRegions[2] = txThree;
		txRegions[3] = txFour;
	}

	@Override
	public void showMenu() {
		if (one == null) {
			one = new Sprite(this.mX - 64, this.mY - 64, txRegions[0],
					this.getVertexBufferObjectManager());
			mScene.attachChild(one);
		} else {
			one.setVisible(true);
		}
		if (two == null) {
			two = new Sprite(this.mX + 64, this.mY - 64, txRegions[1],
					this.getVertexBufferObjectManager());
			mScene.attachChild(two);

		} else {
			two.setVisible(true);
		}
		if (three == null) {
			three = new Sprite(this.mX - 64, this.mY + 64, txRegions[2],
					this.getVertexBufferObjectManager());
			mScene.attachChild(three);
		} else {
			three.setVisible(true);
		}
		if (four == null) {

			four = new Sprite(this.mX + 64, this.mY + 64, txRegions[3],
					this.getVertexBufferObjectManager());
			mScene.attachChild(four);
		} else {
			four.setVisible(true);
		}

		mIsMenuShown = true;
	}

	@Override
	public void hideMenu() {
		one.setVisible(false);
		two.setVisible(false);
		three.setVisible(false);
		four.setVisible(false);
		mIsMenuShown = false;
	}

}
