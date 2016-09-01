package de.bitbrain.braingdx.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.bitbrain.braingdx.core.BrainGdxApp;

public class BrainGdxAppDesktop {
    public static void main(String[] args) {
	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	new LwjglApplication(new BrainGdxApp(), config);
    }
}
