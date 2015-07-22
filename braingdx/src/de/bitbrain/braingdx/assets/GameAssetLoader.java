package de.bitbrain.braingdx.assets;

import java.util.Map;

/**
 * Loads assets of a game
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public interface GameAssetLoader {

    void put(Map<String, Class<?> > assets);
}
