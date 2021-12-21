/*
  Title:
  Super Monty
  ©2020 Awais Khatab

  Target Platform: Android 7.0
  Module: CI360

  Copyright 2020, Awais Khatab, All rights reserved.

 */
package com.badlogic.superMonty;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * BUTTONS
 * TODO: Maybe add controller support @Monty @MagicCube
 */
public class OnScreenControllerRender {
	//TEXTURES
	com.badlogic.superMonty.Map map;
	SpriteBatch batch;
	TextureRegion dpad;
	TextureRegion left;
	TextureRegion right;
	TextureRegion jump;
	TextureRegion magicCubeControl;
	TextureRegion magicCubeFollow;

	/**
	 * SCREEN CONTROLLER RENDER
	 * @param map
	 */
	public OnScreenControllerRender(Map map) {
		this.map = map;
		loadAssets();
	}

	/**
	 * LOAD ASSETS
 	 */
	private void loadAssets () {
		Texture texture = new Texture(Gdx.files.internal("data/controls.png"));
		TextureRegion[] buttons = TextureRegion.split(texture, 64, 64)[0];
		left = buttons[0];
		right = buttons[1];
		jump = buttons[2];
		magicCubeControl = buttons[3];
		magicCubeFollow = TextureRegion.split(texture, 64, 64)[1][2];
		dpad = new TextureRegion(texture, 0, 64, 128, 128);
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
	}

	/**
	 * RENDER
 	 */
	public void show () {
		if (Gdx.app.getType() != ApplicationType.Android && Gdx.app.getType() != ApplicationType.iOS) return;
		if (map.magicCube.state != MagicCube.CONTROLLED) {
			batch.begin();
			batch.draw(left, 0, 0);
			batch.draw(right, 70, 0);
			batch.draw(magicCubeControl, 480 - 64, 320 - 64);
			batch.draw(magicCubeFollow, 480 - 64, 320 - 138);
			batch.draw(jump, 480 - 64, 0);
			batch.end();
		} else {
			batch.begin();
			batch.draw(dpad, 0, 0);
			batch.draw(magicCubeFollow, 480 - 64, 320 - 138);
			batch.draw(magicCubeControl, 480 - 64, 320 - 64);
			batch.end();
		}
	}
	//DISPOSE ASSETS
	public void dispose () {
		dpad.getTexture().dispose();
		batch.dispose();
	}
}
