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
 * TODO:
 */
/**
 * SPIKES MOVEMENT
 */
public class MovingSpikes {
	// FIXED- Private VAR
	private static final int FORWARD = 1;
	private static final int BACKWARD = -1;
	private static final float FORWARD_VEL = 10;
	private static final float BACKWARD_VEL = 4;

	int state = FORWARD;

	Map map;
	Rectangle bounds = new Rectangle();
	Vector2 vel = new Vector2();
	Vector2 pos = new Vector2();
	float angle = 0;
	private int fx = 0;
	private int fy = 0;
	private int bx = 0;
	private int by = 0;

	/**
	 * MOVING SPIKES
	 * @param map
	 * @param x
	 * @param y
	 */
	public MovingSpikes (Map map, float x, float y) {
		this.map = map;
		pos.x = x;
		pos.y = y;
		bounds.x = x;
		bounds.y = y;
		bounds.width = bounds.height = 1;
	}

	/**
	 * TILE ASSIGNMENT VEL
	 */
	public void init () {
		int ix = (int)pos.x;
		int iy = (int)pos.y;

		int left = map.tiles[ix - 1][map.tiles[0].length - 1 - iy];
		int right = map.tiles[ix + 1][map.tiles[0].length - 1 - iy];
		int top = map.tiles[ix][map.tiles[0].length - 1 - iy - 1];
		int bottom = map.tiles[ix][map.tiles[0].length - 1 - iy + 1];

		if (left == Map.TILE) {
			vel.x = FORWARD_VEL;
			angle = -90;
			fx = 1;
		}
		if (right == Map.TILE) {
			vel.x = -FORWARD_VEL;
			angle = 90;
			bx = 1;
		}
		if (top == Map.TILE) {
			vel.y = -FORWARD_VEL;
			angle = 180;
			by = -1;
		}
		if (bottom == Map.TILE) {
			vel.y = FORWARD_VEL;
			angle = 0;
			fy = -1;
		}
	}

	/**
	 *	UPDATE MOVING SPIKES
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		pos.add(vel.x * deltaTime, vel.y * deltaTime);
		boolean change = false;
		if (state == FORWARD) {
			change = map.tiles[(int)pos.x + fx][(int) (map.tiles[0].length - 1 - pos.y + fy)] == Map.TILE;
		} else {
			change = map.tiles[(int)pos.x + bx][(int) (map.tiles[0].length - 1 - pos.y + by)] == Map.TILE;
		}
		if (change) {
			pos.x -= vel.x * deltaTime;
			pos.y -= vel.y * deltaTime;
			state = -state;
			vel.scl(-1);
			if (state == FORWARD) vel.nor().scl(FORWARD_VEL);
			if (state == BACKWARD) vel.nor().scl(BACKWARD_VEL);
		}
		bounds.x = pos.x;
		bounds.y = pos.y;

		//BOUNDS
		if (map.monty.bounds.overlaps(bounds)) {
			if (map.monty.state != com.badlogic.superMonty.Monty.DYING) {
				map.monty.state = Monty.DYING;
				map.monty.stateTime = 0;
			}
		}
		//BOUNDS
		if (map.magicCube.bounds.overlaps(bounds)) {
			map.magicCube.state = MagicCube.DEAD;
			map.magicCube.stateTime = 0;
		}
	}
}
