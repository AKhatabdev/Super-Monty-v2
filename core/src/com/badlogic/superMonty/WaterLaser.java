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
import com.badlogic.gdx.math.Vector2;

/**
 * CLASS: WaterLaser
 * Game Object: Water Laser Logic
 */
public class WaterLaser {
	//VAR STATE
	static final int FORWARD = 1;

	//DO NOT REMOVE
	static final int BACKWARD = -1;
	static final float FORWARD_VEL = 10;
	static final float BACKWARD_VEL = 4;

	int state = FORWARD;

	Map map;
	Vector2 pos = new Vector2();
	Vector2 endPoint = new Vector2();
	Vector2 cappedEndPoint = new Vector2();
	float angle = 0;

	/**
	 * WATER LASER POSITION
	 * @param map
	 * @param x
	 * @param y
	 */
	public WaterLaser(Map map, float x, float y) {
		this.map = map;
		pos.x = x;
		pos.y = y;
	}

	public void init () {
		int ix = (int)pos.x;
		int iy = (int) (map.tiles[0].length - 1 - pos.y);

		int left = map.tiles[ix - 1][iy];
		int right = map.tiles[ix + 1][iy];
		int top = map.tiles[ix][iy - 1];
		int bottom = map.tiles[ix][iy + 1];
		//LEFT POS
		if (left == Map.TILE) {
			angle = -90;
			for (int x = ix; x < map.tiles.length; x++) {
				if (map.tiles[x][iy] == Map.TILE) {
					endPoint.set(x, pos.y);
					break;
				}
			}
		}
		//RIGHT POS
		if (right == Map.TILE) {
			angle = 90;
			for (int x = ix; x >= 0; x--) {
				if (map.tiles[x][iy] == Map.TILE) {
					endPoint.set(x, pos.y);
					break;
				}
			}
		}
		if (top == Map.TILE) {
			angle = 180;
			for (int y = iy; y < map.tiles[0].length; y++) {
				if (map.tiles[ix][y] == Map.TILE) {
					endPoint.set(pos.x, map.tiles[0].length - y - 1);
					break;
				}
			}
		}
		if (bottom == Map.TILE) {
			angle = 0;
			for (int y = iy; y >= 0; y--) {
				if (map.tiles[ix][y] == Map.TILE) {
					endPoint.set(pos.x, map.tiles[0].length - y - 1);
					break;
				}
			}
		}
	}

	Vector2 startPoint = new Vector2();

	/**
	 * UPDATE @WaterLaser
	 */
	public void update () {
		startPoint.set(pos).add(0.5f, 0.5f);
		cappedEndPoint.set(endPoint).add(0.5f, 0.5f);

		Rectangle cbounds = map.magicCube.bounds;
		Rectangle bbounds = map.monty.bounds;

		//BOOLEAN FALSE
		boolean kill = false;

		/**
		 * TODO: Add angle @BOUNDS
		 * TODO: KILL LOGIC
		 */
		if (angle == -90) {
			if (startPoint.x < cbounds.x && endPoint.x > cbounds.x) {
				if (cbounds.y < startPoint.y && cbounds.y + cbounds.height > startPoint.y) {
					cappedEndPoint.x = cbounds.x;
				}
			}
		}
		if (angle == 90) {
			if (startPoint.x > cbounds.x && endPoint.x < cbounds.x) {
				if (cbounds.y < startPoint.y && cbounds.y + cbounds.height > startPoint.y) {
					cappedEndPoint.x = cbounds.x + cbounds.width;
				}
			}
		}
		if (angle == 0) {
			if (startPoint.y < cbounds.y && endPoint.y > cbounds.y) {
				if (cbounds.x < startPoint.x && cbounds.x + cbounds.width > startPoint.x) {
					cappedEndPoint.y = cbounds.y;
				}
			}
		}
		if (angle == 180) {
			if (startPoint.y > cbounds.y && endPoint.y < cbounds.y) {
				if (cbounds.x < startPoint.x && cbounds.x + cbounds.width > startPoint.x) {
					cappedEndPoint.y = cbounds.y + cbounds.height;
				}
			}
		}
		if (angle == -90) {
			if (startPoint.x < bbounds.x) {
				if (bbounds.y < startPoint.y && bbounds.y + bbounds.height > startPoint.y) {
					if (cappedEndPoint.x > bbounds.x) kill = true;
				}
			}
		}
		if (angle == 90) {
			if (startPoint.x > bbounds.x) {
				if (bbounds.y < startPoint.y && bbounds.y + bbounds.height > startPoint.y) {
					if (cappedEndPoint.x < bbounds.x + bbounds.width) kill = true;
				}
			}
		}
		if (angle == 0) {
			if (pos.y < bbounds.y) {
				if (bbounds.x < startPoint.x && bbounds.x + bbounds.width > startPoint.x) {
					if (cappedEndPoint.y > bbounds.y) kill = true;
				}
			}
		}
		if (angle == 180) {
			if (pos.y > bbounds.y) {
				if (bbounds.x < startPoint.x && bbounds.x + bbounds.width > startPoint.x) {
					if (cappedEndPoint.y < bbounds.y + bbounds.height) kill = true;
				}
			}
		}
		/**
		 * STATE LOGIC
		 * @Monty COLLISION @WaterLaser
		 */
		if (kill && map.monty.state != Monty.DYING) {
			map.monty.state = Monty.DYING;
			map.monty.stateTime = 0;
		}
	}
}
