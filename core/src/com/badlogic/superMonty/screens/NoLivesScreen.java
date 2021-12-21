/*
  Title:
  Super Monty
  ©2020 Awais Khatab

  Target Platform: Android 7.0
  Module: CI360

  Copyright 2020, Awais Khatab, All rights reserved.

 */package com.badlogic.superMonty.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.superMonty.GifDecoder;

public class NoLivesScreen extends SuperMontyScreen {

    private final Animation<TextureRegion> animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("data/runOutOfLives.gif").read());
    SpriteBatch batch;
    float time = 0;
    float elapsed;
    private final Sound selectSound = Gdx.audio.newSound(Gdx.files.internal("data/sound_FX/select_sound.wav"));
    private final Music gameOverSound = Gdx.audio.newMusic(Gdx.files.internal("data/sound_FX/gameover_sound.wav"));

    /**
     * NO LIVES SCREEN
     * @param game
     */
    public NoLivesScreen (Game game) { super(game); }

    @Override
    public void show () {
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
        gameOverSound.play();
        gameOverSound.setVolume(0.8f);
        gameOverSound.setLooping(false);
    }

    @Override
    public void render (float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(animation.getKeyFrame(elapsed), 20.0f, 20.0f);
        batch.end();

        time += delta;
        if (time > 1) {
            if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched()) {
                selectSound.play(1.0f);
                Gdx.input.vibrate(50);
                gameOverSound.stop();
                game.setScreen(new GameScreen(game));
            }
        }
    }

    @Override
    public void hide () {
        Gdx.app.debug("Super Monty", "dispose intro");
        selectSound.dispose();
        gameOverSound.dispose();
        batch.dispose();
    }
}
