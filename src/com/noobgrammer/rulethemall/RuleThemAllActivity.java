package com.noobgrammer.rulethemall;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.noobgrammer.rulethemall.towers.BaseTower;
import com.noobgrammer.rulethemall.units.Critter;
import com.noobgrammer.rulethemall.units.Human;
import com.noobgrammer.rulethemall.units.path.Track;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 18:47:08 - 19.03.2010
 */

// RealMayo: THIS PROJECT IS SIMPLY THE CODE TAKEN FROM PhysicsExample.java FROM THE AndEngineExamples

public class RuleThemAllActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
	
	private static final short CRITTER_CATMASK = 0x0001;
	private static final short TOWER_CATMASK = 0x0002;
	private static final short HUMAN_CATMASK = 0x0004;
	private static final short CRITTER_GROUP = 0x0001;
	private static final short TOWER_GROUP = 0x0002;
	private static final short HUMAN_GROUP = 0x0004;

	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
	private static final FixtureDef FIXTURE_DEF_TOWER = PhysicsFactory.createFixtureDef(1, 0f, 0f, true, TOWER_CATMASK, CRITTER_CATMASK, TOWER_GROUP);
	private static final FixtureDef FIXTURE_DEF_CRITTER = PhysicsFactory.createFixtureDef(1, 0f, 0f, false, CRITTER_CATMASK, (short)(TOWER_CATMASK | HUMAN_CATMASK), CRITTER_GROUP);
	private static final FixtureDef FIXTURE_DEF_HUMAN = PhysicsFactory.createFixtureDef(1, 0f, 0f, true, HUMAN_CATMASK, CRITTER_CATMASK, HUMAN_GROUP);
	
	
	private Critter face_;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private BitmapTextureAtlas mBackgroundBitmapTextureAtlas;

	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;
	private TiledTextureRegion mTriangleFaceTextureRegion;
	private TiledTextureRegion mHexagonFaceTextureRegion;
	private SpriteBackground mBackgroundSprite;

	private Scene mScene;

	private PhysicsWorld mPhysicsWorld;
	private int mFaceCount = 0;
	private TextureRegion mBgTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
	}

	@Override   
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 128, TextureOptions.BILINEAR);
		mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "critter_.png", 0, 0, 1, 1); // 64x32
		mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32
		mTriangleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_triangle_tiled.png", 0, 64, 2, 1); // 64x32
		mHexagonFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_hexagon_tiled.png", 0, 96, 2, 1); // 64x32
		mBitmapTextureAtlas.load();

		mBackgroundBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 800, 480, TextureOptions.BILINEAR);
		mBgTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundBitmapTextureAtlas, this, "kr_1.png", 0, 0);
		mBackgroundBitmapTextureAtlas.load(); 
		
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		mBackgroundSprite = new SpriteBackground(new Sprite(0, 0, mBgTextureRegion, this.getVertexBufferObjectManager()));
		this.mScene.setBackground(mBackgroundSprite);
		this.mScene.setBackgroundEnabled(true);
		this.mScene.setOnSceneTouchListener(this);

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_DEATH_STAR_I), false);
		this.mPhysicsWorld.setContactListener(new ContactListener() {
            @Override
			public void beginContact(Contact contact)
			{
				// TODO Auto-generated method stub
            	Fixture f1 = contact.getFixtureA();
            	Fixture f2 = contact.getFixtureB();
            	if(f1.isSensor() ^ f2.isSensor())
            	{
            		Fixture sensor;
            		Fixture sensed;
            		if(f1.isSensor())
            		{
            			sensor = f1;
            			sensed = f2;
            		}
            		else
            		{
            			sensor = f2;
            			sensed = f1;
            		}
            		if(sensor.getBody().getUserData() instanceof BaseTower)
            		{
            			BaseTower tower = (BaseTower) f2.getBody().getUserData();
            			tower.animate(200);
            		}
            		else if(sensor.getBody().getUserData() instanceof Human)
            		{
            			Human human = (Human) sensor.getBody().getUserData();;
            			human.acquireTarget((Critter) sensed.getBody().getUserData());
            		}
            	}
            	
				
			}

			@Override
			public void endContact(Contact contact)
			{
				Fixture f1 = contact.getFixtureA();
            	Fixture f2 = contact.getFixtureB();
            	if(f1.isSensor() ^ f2.isSensor())
            	{
            		Fixture sensor = f1.isSensor() ? f1 : f2;
            		if(sensor.getBody().getUserData() instanceof BaseTower)
            		{
            			BaseTower tower = (BaseTower) f2.getBody().getUserData();
            			tower.stopAnimation(0);
            		}
            		else if(sensor.getBody().getUserData() instanceof Human)
            		{
            			
            		}
            	}
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold)
			{
//				Log.d("LEM!", "" + contact.getFixtureA().getUserData() + " " + contact.getFixtureB().getUserData());
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse)
			{
				// TODO Auto-generated method stub
				
			}
    });

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		
		
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);
		

		this.mScene.attachChild(ground);
		this.mScene.attachChild(roof);
		this.mScene.attachChild(left);
		this.mScene.attachChild(right);
		

		this.addFace(0, 0);
		addHuman();
		this.addTower(200, 100);

		this.mScene.registerUpdateHandler(this.mPhysicsWorld);
		this.mScene.registerUpdateHandler(new IUpdateHandler()
		{
			// Parang main game loop
			@Override
			public void onUpdate(float pSecondsElapsed)
			{
//				if(body_ != null && face_ != null)
//					body_.setTransform(face_.getX()/32, face_.getY()/32, 0);
//					body_.setTransform(face_.getX()/32 + face_.getWidth()/2, face_.getY()/32 + face_.getHeight()/2, 0);
			}

			@Override
			public void reset()
			{
				
			}
			
		});
//TODO DEBUG DRAW DEBUGDRAW - TOGGLE ON OFF  	 		
//	    mScene.attachChild(new DebugRenderer(mPhysicsWorld, getVertexBufferObjectManager()));	

		return this.mScene;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(this.mPhysicsWorld != null) {
			if(pSceneTouchEvent.isActionDown()) {
				{
//					this.addTower(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		this.disableAccelerationSensor();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void addFace(final float pX, final float pY) {
		this.mFaceCount++;
		Debug.d("Faces: " + this.mFaceCount);
		
		face_ = new Critter(pX, pY, this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager(), this.mPhysicsWorld, FIXTURE_DEF_CRITTER);
		
		        /* Add the proper animation when a waypoint of the path is passed. */
        face_.setPath(Track.LevelOnePath());
        
		this.mScene.attachChild(face_);
	}
	
	private void addTower(final float pX, final float pY)
	{
		BaseTower tower = new BaseTower(pX, pY, this.mCircleFaceTextureRegion, this.getVertexBufferObjectManager(), this.mPhysicsWorld, FIXTURE_DEF_TOWER);
		this.mScene.attachChild(tower);
		mScene.registerTouchArea(tower);
	}
	
	private void addHuman()
	{
		Human human = new Human(26, CAMERA_HEIGHT - 74, this.mTriangleFaceTextureRegion, this.getVertexBufferObjectManager(), this.mPhysicsWorld, FIXTURE_DEF_HUMAN);
		this.mScene.attachChild(human);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}



