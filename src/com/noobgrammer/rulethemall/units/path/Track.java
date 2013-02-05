package com.noobgrammer.rulethemall.units.path;

import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.util.modifier.ease.EaseLinear;

public class Track {
	
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
		
	
	public static IEntityModifier LevelOnePath()
	{
		final Path path = new Path(5).to(10, 10).to(10, CAMERA_HEIGHT - 74).to(CAMERA_WIDTH - 58, CAMERA_HEIGHT - 74).to(CAMERA_WIDTH - 58, 10).to(10, 10);
		return ((new PathModifier(30, path, EaseLinear.getInstance())));
	}

}
