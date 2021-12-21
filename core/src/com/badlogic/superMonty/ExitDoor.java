/*
  Title:
  Super Monty
  ©2020 Awais Khatab

  Target Platform: Android 7.0
  Module: CI360

  Copyright 2020, Awais Khatab, All rights reserved.

 */
package com.badlogic.superMonty;

import com.badlogic.gdx.math.Rectangle;

public class ExitDoor {
	public Rectangle bounds = new Rectangle();

	/**
	 * EXIT
	 * @param x
	 * @param y
	 */
	public ExitDoor (float x, float y) {
		this.bounds.x = x;
		this.bounds.y = y;
		this.bounds.width = this.bounds.height = 1;
	}
}
