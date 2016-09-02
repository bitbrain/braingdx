package de.bitbrain.braingdx.html;

import de.bitbrain.braingdx.core.BrainGdxApp;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class BrainGdxAppHtml extends GwtApplication {
    @Override
    public ApplicationListener getApplicationListener() {
	return new BrainGdxApp();
    }

    @Override
    public GwtApplicationConfiguration getConfig() {
	return new GwtApplicationConfiguration(480, 320);
    }
}
