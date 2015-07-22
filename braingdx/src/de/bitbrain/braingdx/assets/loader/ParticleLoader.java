package de.bitbrain.braingdx.assets.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Array;

/**
 * Created by miguel on 20.07.15.
 */
public class ParticleLoader extends SynchronousAssetLoader<ParticleEffect, ParticleLoader.ParticleParameter> {

    public ParticleLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    static public class ParticleParameter extends AssetLoaderParameters<ParticleEffect> {

    }

    @SuppressWarnings("rawtypes")
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ParticleParameter parameter) {
        return null;
    }

    @Override
    public ParticleEffect load(AssetManager assetManager, String fileName, FileHandle file, ParticleParameter parameter) {
        ParticleEffect effect = new ParticleEffect();
        effect.load(file, Gdx.files.internal("particles/"));
        return effect;
    }
}
