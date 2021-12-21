/*
  Title:
  Super Monty
  ©2020 Awais Khatab

  Target Platform: Android 7.0
  Module: CI360

  Copyright 2020, Awais Khatab, All rights reserved.

 */
package com.badlogic.superMonty;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;

/**
 * MAP PIXMAP
 */
public class Map {
	// Pixel Values 32int in HEX Colour
	static int EMPTY = 0;
	static int TILE = 0xffffff;
	static int WATER = 0x00ff00;
	static int ROCKET = 0x0000ff;
	static int MOVING_SPIKES = 0xffff00;
	static int LASER = 0x00ffff;
	static int START = 0xff0000;
	static int END = 0xff00ff;
	static int SAVESPOT = 0xff0100;
	//2D
	int[][] tiles;

	public com.badlogic.superMonty.Monty monty;

	MagicCube magicCube;
	Array<SaveSpot> saveSpots = new Array<SaveSpot>();
	SaveSpot activeSaveSpot = null;
	Array<com.badlogic.superMonty.Rocket> rockets = new Array<com.badlogic.superMonty.Rocket>();
	Array<com.badlogic.superMonty.MovingSpikes> movingSpikes = new Array<com.badlogic.superMonty.MovingSpikes>();
	Array<WaterLaser> waterLasers = new Array<WaterLaser>();
	public com.badlogic.superMonty.ExitDoor exitDoor;

	public Map () {
		loadBinary();
	}
	/**
	 * Load Binary MAP @Pixmap LIBGDX
	 */

	private void loadBinary () {
		//Level data from image, SWITCH method for Level Selection @GameScreen input 1-3
		int levelSelect = com.badlogic.superMonty.screens.GameScreen.getLevelSelector();
		String level;
		//Asset Theme Selection @GameScreen LevelSelector
		switch(levelSelect){
			case 1:
				level = ("data/level_data/level_1.png");
				break;
			case 2:
				level = ("data/level_data/level_2.png");
				break;
			case 3:
				level = ("data/level_data/level_3.png");
				break;
			default:
				throw new IllegalStateException("Level Data Error " + levelSelect);
		}

		Pixmap pixmap = new Pixmap(Gdx.files.internal(level));

		tiles = new int[pixmap.getWidth()][pixmap.getHeight()];
		/**
		 * PIXEL DATA FROM LOADED IMAGE
		 * (FOR) ASSETS/OBJECTS LOCATIONS
		 */
		for (int y = 0; y < 35; y++) {
			for (int x = 0; x < 150; x++) {
				int pix = (pixmap.getPixel(x, y) >>> 8) & 0xffffff;
				if (match(pix, START)) {
					SaveSpot savespot = new SaveSpot(x, pixmap.getHeight() - 1 - y);
					saveSpots.add(savespot);
					activeSaveSpot = savespot;
					monty = new com.badlogic.superMonty.Monty(this, activeSaveSpot.bounds.x, activeSaveSpot.bounds.y);
					monty.state = com.badlogic.superMonty.Monty.SPAWN;
					magicCube = new MagicCube(this, activeSaveSpot.bounds.x, activeSaveSpot.bounds.y);
					magicCube.state = MagicCube.DEAD;

				} else if (match(pix, SAVESPOT)) {
					com.badlogic.superMonty.SaveSpot savespot = new com.badlogic.superMonty.SaveSpot(x, pixmap.getHeight() - 1 - y);
					saveSpots.add(savespot);

				} else if (match(pix, ROCKET)) {
					com.badlogic.superMonty.Rocket rocket = new com.badlogic.superMonty.Rocket(this, x, pixmap.getHeight() - 1 - y);
					rockets.add(rocket);

				} else if (match(pix, MOVING_SPIKES)) {
					movingSpikes.add(new com.badlogic.superMonty.MovingSpikes(this, x, pixmap.getHeight() - 1 - y));

				} else if (match(pix, LASER)) {
					waterLasers.add(new WaterLaser(this, x, pixmap.getHeight() - 1 - y));

				} else if (match(pix, END)) {
					//FATAL EXCEPTION GLTHREAD @GameScreen Render *FIXED*
					exitDoor = new ExitDoor(x, pixmap.getHeight() - 1 - y);
				} else {
					tiles[x][y] = pix;
				}
			}
		}

		for (int i = 0; i < movingSpikes.size; i++) {
			movingSpikes.get(i).init();
		}
		for (int i = 0; i < waterLasers.size; i++) {
			waterLasers.get(i).init();
		}
	}

	boolean match (int src, int dst) {
		return src == dst;
	}

	/**
	 * UPDATE
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		monty.update(deltaTime);
		if (monty.state == com.badlogic.superMonty.Monty.DEAD) monty = new Monty(this, activeSaveSpot.bounds.x, activeSaveSpot.bounds.y);
		magicCube.update(deltaTime);
		if (magicCube.state == MagicCube.DEAD) magicCube = new MagicCube(this, monty.bounds.x, monty.bounds.y);
		for (int i = 0; i < saveSpots.size; i++) {
			if (monty.bounds.overlaps(saveSpots.get(i).bounds)) {
				activeSaveSpot = saveSpots.get(i);
			}
		}
		//ROCKET AR @DeltaTime
		for (int i = 0; i < rockets.size; i++) {
			Rocket rocket = rockets.get(i);
			rocket.update(deltaTime);
		}
		//M SPIKE AR @DeltaTime
		for (int i = 0; i < movingSpikes.size; i++) {
			MovingSpikes spikes = movingSpikes.get(i);
			spikes.update(deltaTime);
		}
		//WATER LASER AR @DeltaTime
		for (int i = 0; i < waterLasers.size; i++) {
			waterLasers.get(i).update();
		}

	}

	/**
	 * Asset Deadly Logic WATER
	 * @param tileId
	 * @return
	 */
	public boolean isDeadly (int tileId) {
		return tileId == WATER;
	}

}
