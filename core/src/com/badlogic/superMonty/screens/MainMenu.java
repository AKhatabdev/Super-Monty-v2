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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.superMonty.GifDecoder;

public class MainMenu extends SuperMontyScreen {

	private final Animation<TextureRegion> animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("data/title.gif").read());
	SpriteBatch batch;
	float time = 0; //Delay timer
	float elapsed;

	//Music and Sound Effects are from an Open Source platform: https://www.epidemicsound.com/
	private final Sound selectSound = Gdx.audio.newSound(Gdx.files.internal("data/sound_FX/select_sound.wav"));
	private final Music mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("data/sound_FX/bgMainMenu.wav"));

	/**
	 * MAIN MENU
	 * @param game
	 */
	public MainMenu (Game game) {
		super(game);
	}

	@Override
	public void show () {
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
		mainMenuMusic.play();
		mainMenuMusic.setVolume(1.0f);
		mainMenuMusic.setLooping(true);
	}

	@Override
	public void render (float delta) {
		elapsed += Gdx.graphics.getDeltaTime();
		animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(animation.getKeyFrame(elapsed), 20.0f, 20.0f);
		batch.end();

		time += delta;
		if (time > 3) {
			if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched()) {
				selectSound.play(1.0f);
				Gdx.input.vibrate(50);
				mainMenuMusic.stop();
				game.setScreen(new IntroScreen(game));
			}
		}
	}

	@Override
	public void hide () {
		Gdx.app.debug("Super Monty", "dispose main menu");
		selectSound.dispose();
		mainMenuMusic.dispose();
		batch.dispose();
	}
}
