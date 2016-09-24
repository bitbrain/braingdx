package com.bitfire.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ClassPathResolver implements PathResolver {

    @Override
    public FileHandle resolve(String path) {
	return Gdx.files.classpath(path);
    }

}
