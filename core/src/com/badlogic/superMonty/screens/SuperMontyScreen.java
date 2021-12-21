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
import com.badlogic.gdx.Screen;

public abstract class SuperMontyScreen implements Screen {
	Game game;

	/**
	 * SUPER MONTY SCREEN
	 * @param game
	 */
	public SuperMontyScreen(Game game) {
		this.game = game;
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {

	}

	@Override
	public void resume () {

	}

	@Override
	public void dispose () {
	}
}
/* TODO: Remove excess and unnecessary comments through out project after completion. */