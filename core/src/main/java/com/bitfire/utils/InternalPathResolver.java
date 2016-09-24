package com.bitfire.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class InternalPathResolver implements PathResolver {

    @Override
    public FileHandle resolve(String path) {
	return Gdx.files.internal(path);
    }

}
