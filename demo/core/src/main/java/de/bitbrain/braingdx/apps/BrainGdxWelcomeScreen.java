package de.bitbrain.braingdx.apps;

import com.badlogic.gdx.graphics.Color;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.screens.AbstractScreen;

public class BrainGdxWelcomeScreen extends AbstractScreen<BrainGdxTest> {

	public BrainGdxWelcomeScreen(BrainGdxTest game) {
		super(game);
		setBackgroundColor(Color.WHITE);
	}

	@Override
	protected void onCreate(GameContext context) {
		// noOp
	}

}
