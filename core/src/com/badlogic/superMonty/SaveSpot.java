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

/**
 * DISPENSER
 */
public class SaveSpot {
	Rectangle bounds = new Rectangle();
	boolean active = false;

	public SaveSpot(float x, float y) {
		bounds.x = x;
		bounds.y = y;
		bounds.width = bounds.height = 1;
	}
}
