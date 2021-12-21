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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.superMonty.Lives;
import com.badlogic.superMonty.Map;
import com.badlogic.superMonty.MapRenderer;
import com.badlogic.superMonty.OnScreenControllerRender;

public class GameScreen extends SuperMontyScreen {
	com.badlogic.superMonty.Map map;
	com.badlogic.superMonty.MapRenderer renderer;
	OnScreenControllerRender controlRenderer;
	Lives livesRenderer;

	float time = 0;

	public static int levelSelector = 1;

	public static int getLevelSelector(){ return levelSelector; }

	public static void setLevelSelector(int setLevel){ GameScreen.levelSelector = setLevel; }

	private final Music bgMusic = Gdx.audio.newMusic(Gdx.files.internal("data/sound_FX/bgGameMusic.wav"));
	private final Music lowHeart = Gdx.audio.newMusic(Gdx.files.internal("data/sound_FX/lowHeart_sound.wav"));
	//private Sound levelComplete = Gdx.audio.newSound(Gdx.files.internal("data/sound_FX/endMusic.wav"));
	/**
	 * GAME SCREEN
	 * @param game
	 */
	public GameScreen (Game game) {
		super(game);
	}

	@Override
	public void show () {
		map = new Map();
		renderer = new MapRenderer(map);
		controlRenderer = new OnScreenControllerRender(map);
		livesRenderer = new Lives(map);

		bgMusic.play();
		bgMusic.setVolume(1.0f);                 // sets the volume to half the maximum volume
		bgMusic.setLooping(true);                // will repeat playback until music.stop() is called
	}

	@Override
	public void render (float delta) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
		map.update(delta);
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.render(delta);
		controlRenderer.show();
		livesRenderer.render();

		//Monty overlaps Exit
		if (map.monty.bounds.overlaps(map.exitDoor.bounds)) {
			//Delay for entering door
			time += delta;

			if (time > 2){
			    switch (levelSelector){
                    case 1:
                    	//LEVEL 1
                        bgMusic.stop();
                        Gdx.input.vibrate(50);
                        setLevelSelector(2);
                        game.setScreen(new Level2IntroScreen(game));
                        break;
                    case 2:
                    	//LEVEL 2
                        bgMusic.stop();
                        Gdx.input.vibrate(50);
                        setLevelSelector(3);
                        game.setScreen(new Level3IntroScreen(game));
                        break;
					case 3:
						//LEVEL 3
						bgMusic.stop();
						Gdx.input.vibrate(50);
						setLevelSelector(1);
						game.setScreen(new GameOverScreen(game));
						break;
					default:
						throw new IllegalStateException("GameScreen Level Assignment Error" + levelSelector);
                }
			}
		}

		//MONTY LIVES GETTER AND SETTER
		int montyLIFE = com.badlogic.superMonty.Monty.getLIVES();
		//Monty Life Logic
		if (montyLIFE == 0){
			bgMusic.stop();
			lowHeart.stop();
			Gdx.input.vibrate(100);
			com.badlogic.superMonty.Monty.setLIVES(6);
			game.setScreen(new NoLivesScreen(game));
		}
		else if(montyLIFE == 1) {
			lowHeart.play();
			lowHeart.setVolume(0.5f);
			lowHeart.setLooping(true);
		}

		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.input.vibrate(50);
			game.setScreen(new MainMenu(game));
		}
	}

	@Override
	public void hide () {
		Gdx.app.debug("Super Monty", "dispose game screen");
		bgMusic.dispose();
		lowHeart.dispose();
		renderer.dispose();
		controlRenderer.dispose();
		livesRenderer.dispose();
	}
}
