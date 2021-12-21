/*
  Title:
  Super Monty
  ©2020 Awais Khatab

  Target Platform: Android 7.0
  Module: CI360

  Copyright 2020, Awais Khatab, All rights reserved.

 */
package com.badlogic.superMonty.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.superMonty.GifDecoder;

public class SplashScreen extends SuperMontyScreen{

    float time = 0;
    float elapsed;

    private final Animation<TextureRegion> animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("data/splashScreen.gif").read());
    SpriteBatch batch;
    private final Sound selectSound = Gdx.audio.newSound(Gdx.files.internal("data/sound_FX/select_sound.wav"));

    /**
     * SPLASH SCREEN
     * @param game
     */
    public SplashScreen(Game game) {
        super(game);
    }

    @Override
    public void show () {
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
    }

    @Override
    public void render (float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        batch.begin();
        batch.draw(animation.getKeyFrame(elapsed), 20.0f, 20.0f);
        batch.end();

        time += delta;
        if (time > 5) {
                selectSound.play(1.0f);
                Gdx.input.vibrate(50);
                game.setScreen(new MainMenu(game));
        }
    }

    @Override
    public void hide () {
        Gdx.app.debug("Super Monty", "dispose intro");
        selectSound.dispose();
        batch.dispose();
    }
}
