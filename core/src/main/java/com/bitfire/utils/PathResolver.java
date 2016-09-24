package com.bitfire.utils;

import com.badlogic.gdx.files.FileHandle;

public interface PathResolver {
    FileHandle resolve(String path);
}
