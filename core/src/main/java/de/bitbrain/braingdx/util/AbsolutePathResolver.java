package de.bitbrain.braingdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class AbsolutePathResolver implements PathResolver {

    @Override
    public FileHandle resolve(String path) {
	return Gdx.files.classpath(path);
    }

}
