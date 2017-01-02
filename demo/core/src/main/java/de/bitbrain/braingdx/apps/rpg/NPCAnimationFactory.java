package de.bitbrain.braingdx.apps.rpg;

import java.util.Map;

import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimation;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimation.Direction;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;

public class NPCAnimationFactory {

    public static class Index {
	public final int x;
	public final int y;

	public Index(int x, int y) {
	    this.x = x;
	    this.y = y;
	}
    }

    private static final Index DEFAULT_INDEX = new Index(0, 0);

    private final Map<Integer, Index> indices;

    private final SpriteSheet sheet;

    public NPCAnimationFactory(SpriteSheet sheet, Map<Integer, Index> indices) {
	this.sheet = sheet;
	this.indices = indices;
    }

    public SpriteSheetAnimation create(int type) {
	Index index = indices.get(type);
	if (sheet == null) {
	    index = DEFAULT_INDEX;
	}
	return new SpriteSheetAnimation(sheet)
		.origin(index.x, index.y)
		.interval(0.15f)
	         .direction(Direction.HORIZONTAL)
		 .type(AnimationTypes.RESET)
	         .base(1)
	         .frames(3);
    }
}
