package de.bitbrain.braingdx.util;

import com.badlogic.gdx.files.FileHandle;

public interface PathResolver {
    FileHandle resolve(String path);
}
