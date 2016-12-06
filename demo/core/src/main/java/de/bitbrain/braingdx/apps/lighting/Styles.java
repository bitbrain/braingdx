package de.bitbrain.braingdx.apps.lighting;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import de.bitbrain.braingdx.apps.Assets;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.util.Colors;

final class Styles {

    public static final TextButtonStyle BUTTON_DEFAULT_ACTIVE = new TextButtonStyle();
    public static final TextButtonStyle BUTTON_DEFAULT_INACTIVE = new TextButtonStyle();

    public static void init() {
	BUTTON_DEFAULT_ACTIVE.font = SharedAssetManager.getInstance().get(Assets.FONT_BYOM_32, BitmapFont.class);
	BUTTON_DEFAULT_ACTIVE.fontColor = Colors.COLOR_UI_ACTIVE.cpy();
	BUTTON_DEFAULT_ACTIVE.overFontColor = Colors.COLOR_UI_ACTIVE.add(-0.2f, -0.2f, -0.2f, 0f);
	Texture buttonTexture = SharedAssetManager.getInstance().get(Assets.BUTTON_DEFAULT, Texture.class);
	BUTTON_DEFAULT_ACTIVE.up = new NinePatchDrawable(
		GraphicsFactory.createNinePatch(buttonTexture, 10, BUTTON_DEFAULT_ACTIVE.fontColor));
	BUTTON_DEFAULT_ACTIVE.over = new NinePatchDrawable(
		GraphicsFactory.createNinePatch(buttonTexture, 10, BUTTON_DEFAULT_ACTIVE.overFontColor));

	BUTTON_DEFAULT_INACTIVE.font = SharedAssetManager.getInstance().get(Assets.FONT_BYOM_32, BitmapFont.class);
	BUTTON_DEFAULT_INACTIVE.fontColor = Colors.COLOR_UI_INACTIVE.cpy();
	BUTTON_DEFAULT_INACTIVE.overFontColor = Colors.COLOR_UI_INACTIVE.add(-0.2f, -0.2f, -0.2f, 0f);
	BUTTON_DEFAULT_INACTIVE.up = new NinePatchDrawable(
		GraphicsFactory.createNinePatch(buttonTexture, 10, BUTTON_DEFAULT_INACTIVE.fontColor));
	BUTTON_DEFAULT_INACTIVE.over = new NinePatchDrawable(
		GraphicsFactory.createNinePatch(buttonTexture, 10, BUTTON_DEFAULT_INACTIVE.overFontColor));
    }
}
