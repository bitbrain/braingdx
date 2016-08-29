package de.bitbrain.braingdx.tweening;

import de.bitbrain.braingdx.tweening.paths.CatmullRom;
import de.bitbrain.braingdx.tweening.paths.Linear;

/**
 * Collection of built-in paths.
 *
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public interface TweenPaths {
	public static final Linear linear = new Linear();
	public static final CatmullRom catmullRom = new CatmullRom();
}
