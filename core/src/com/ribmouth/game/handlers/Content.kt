package com.ribmouth.game.handlers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable

/**
 * Created by RibMouth on 31/10/2017.
 */
class Content: Disposable {
    private var textureAtlases: HashMap<String, TextureAtlas> = HashMap()

    //TextureAtlases
    fun loadAtlas(path: String, key: String) {
        textureAtlases.put(key, TextureAtlas(Gdx.files.internal(path)))
    }

    fun getAtlas(key: String): TextureAtlas? {
        return textureAtlases[key]
    }

    override fun dispose() {
        TODO("not implemented")
    }
}