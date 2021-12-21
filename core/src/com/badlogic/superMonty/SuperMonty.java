/*
  Title:
  Super Monty
  ©2020 Awais Khatab

  Target Platform: Android 7.0
  Module: CI360

  Copyright 2020, Awais Khatab, All rights reserved.

 */
package com.badlogic.superMonty;

import com.badlogic.gdx.Game;
import com.badlogic.superMonty.screens.SplashScreen;

/**
 * MAIN CREATE GAME
 */
public class SuperMonty extends Game {
	@Override
	public void create () {
		setScreen(new SplashScreen(this));
	}
}
