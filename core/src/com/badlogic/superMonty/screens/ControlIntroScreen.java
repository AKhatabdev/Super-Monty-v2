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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ControlIntroScreen extends SuperMontyScreen {
    TextureRegion introControl;
    SpriteBatch batch;
    float time = 0;

    private final Sound selectSound = Gdx.audio.newSound(Gdx.files.internal("data/sound_FX/select_sound.wav"));
    /**
     * CONTROL SCREEN EXPLANATION SCREEN
     * @param game
     */
    public ControlIntroScreen (Game game) {
        super(game);
    }
    
    @Override
    public void show () {
        introControl = new TextureRegion(new Texture(Gdx.files.internal("data/introControls.png")), 0, 0, 480, 320);
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
    }

    @Override
    public void render (float delta) {
        try{
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            batch.draw(introControl, 0, 0);
            batch.end();

            time += delta;
            if (time > 2) {
                if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched()) {
                    selectSound.play(1.0f);
                    Gdx.input.vibrate(50);
                    game.setScreen(new Level1IntroScreen(game));
                }
            }
        } catch(Exception e){
            System.out.println("Failed to show Controller Screen");
            Gdx.app.debug("Error", "Failed to render Controller Screen");
        }

    }

    @Override
    public void hide () {
        Gdx.app.debug("Super Monty", "dispose intro");
        selectSound.dispose();
        batch.dispose();
        introControl.getTexture().dispose();
    }

}

