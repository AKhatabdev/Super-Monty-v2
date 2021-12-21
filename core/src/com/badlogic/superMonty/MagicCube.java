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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * MAGI THE MAGICAL CUBE
 */
class MagicCube {
	static final int FOLLOW = 0;
	static final int FIXED = 1;
	static final int CONTROLLED = 2;
	static final int DEAD = 3;
	static final float ACCELERATION = 20;
	//MAX_VELOCITY Adjust *FIXED
	static final float MAX_VELOCITY = 6;
	static final float DAMP = 0.80f;

	private final Sound MagicCubeControlled = Gdx.audio.newSound(Gdx.files.internal("data/sound_FX/magicCube_Using.ogg"));
	private final Sound MagicCubeFollowing = Gdx.audio.newSound(Gdx.files.internal("data/sound_FX/magicCube_CallBack.ogg"));

	Map map;
	Vector2 pos = new Vector2();
	Vector2 acceleration = new Vector2();
	Vector2 vel = new Vector2();
	Rectangle bounds = new Rectangle();
	int state = FOLLOW;
	float stateTime = 0;
	Rectangle controlButtonRect = new Rectangle(480 - 64, 320 - 64, 64, 64);
	Rectangle followButtonRect = new Rectangle(480 - 64, 320 - 138, 64, 64);
	Rectangle dpadRect = new Rectangle(0, 0, 128, 128);
	Vector2 target = new Vector2();
	/**
	 * MagicCube Map Pos
	 * @param map
	 * @param x
	 * @param y
	 */
	public MagicCube (Map map, float x, float y) {
		this.map = map;
		this.pos.x = x;
		this.pos.y = y;
		this.bounds.x = pos.x + 0.2f;
		this.bounds.y = pos.y + 0.2f;
		this.bounds.width = this.bounds.height = 1.0f;
	}

	/**
	 * UPDATE
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		processKeys();
		if (state == FOLLOW) {
			target.set(map.monty.position);
			if (map.monty.dir == Monty.RIGHT) target.x--;
			if (map.monty.dir == Monty.LEFT) target.x++;
			target.y += 0.2f;
			//*FIXED* Changed Math.min(5 to 8, pos)
			vel.set(target).sub(pos).scl(Math.min(10, pos.dst(target)) * deltaTime);
			if (vel.len() > MAX_VELOCITY) vel.nor().scl(MAX_VELOCITY);
			tryMove();
		}

		/**
 		* CONTROLLED STATE
 		*/
		if (state == CONTROLLED) {
			acceleration.scl(deltaTime);
			vel.add(acceleration.x, acceleration.y);
			if (acceleration.x == 0) vel.x *= DAMP;
			if (acceleration.y == 0) vel.y *= DAMP;
			if (vel.x > MAX_VELOCITY) vel.x = MAX_VELOCITY;
			if (vel.x < -MAX_VELOCITY) vel.x = -MAX_VELOCITY;
			if (vel.y > MAX_VELOCITY) vel.y = MAX_VELOCITY;
			if (vel.y < -MAX_VELOCITY) vel.y = -MAX_VELOCITY;
			vel.scl(deltaTime);
			tryMove();
			vel.scl(1.0f / deltaTime);
		}
		if (state == FIXED) {
			if (stateTime > 5.0f) {
				stateTime = 0;
				state = FOLLOW;
			}
		}

		stateTime += deltaTime;
	}

	/**
	 * PROCESSING USER INPUT
	 */
	private void processKeys () {
		float x0 = (Gdx.input.getX(0) / (float)Gdx.graphics.getWidth()) * 480;
		float x1 = (Gdx.input.getX(1) / (float)Gdx.graphics.getWidth()) * 480;
		float y0 = 320 - (Gdx.input.getY(0) / (float)Gdx.graphics.getHeight()) * 320;
		float y1 = 320 - (Gdx.input.getY(1) / (float)Gdx.graphics.getHeight()) * 320;
		boolean controlButton = (Gdx.input.isTouched(0) && controlButtonRect.contains(x0, y0))
			|| (Gdx.input.isTouched(1) && controlButtonRect.contains(x1, y1));
		boolean followButton = (Gdx.input.isTouched(0) && followButtonRect.contains(x0, y0))
			|| (Gdx.input.isTouched(1) && followButtonRect.contains(x1, y1));

		if ((Gdx.input.isKeyPressed(Keys.SPACE) || controlButton) && state == FOLLOW && stateTime > 0.5f) {
			stateTime = 0;
			state = CONTROLLED;
			//Sound - Vibration
			MagicCubeControlled.play(1.0f);
			Gdx.input.vibrate(50);
			return;
		}

		if ((Gdx.input.isKeyPressed(Keys.SPACE) || controlButton) && state == CONTROLLED && stateTime > 0.5f) {
			stateTime = 0;
			state = FIXED;
			MagicCubeControlled.play(1.0f, 2.0f, 0);
			Gdx.input.vibrate(50);
			return;
		}

		if ((Gdx.input.isKeyPressed(Keys.SPACE) || controlButton) && state == FIXED && stateTime > 0.5f) {
			stateTime = 0;
			state = CONTROLLED;
			MagicCubeControlled.play(1.0f);
			Gdx.input.vibrate(50);
			return;
		}

		if ((Gdx.input.isKeyPressed(Keys.F) || followButton) && stateTime > 0.5f) {
			stateTime = 0;
			state = FOLLOW;
			MagicCubeControlled.play(1.0f,0.5f,0);
			Gdx.input.vibrate(50);
			return;
		}

		boolean touch0 = Gdx.input.isTouched(0);
		boolean touch1 = Gdx.input.isTouched(1);
		boolean right = (touch0 && (x0 > 80 && x0 < 128)) || (touch1 && (x1 > 80 && x1 < 128));
		boolean down = (touch0 && (y0 < 60)) || (touch1 && (y1 < 60));
		boolean up = (touch0 && (y0 > 80 && x0 < 128)) || (touch1 && (y1 > 80 && y1 < 128));

		/**
 		* CONTROLLED STATE
 		*/
		if (state == CONTROLLED) {
			if (Gdx.input.isKeyPressed(Keys.A)) {
				acceleration.x = -ACCELERATION;
			} else if (Gdx.input.isKeyPressed(Keys.D) || right) {
				acceleration.x = ACCELERATION;
			} else {
				acceleration.x = 0;
			}

			if (Gdx.input.isKeyPressed(Keys.W) || up) {
				acceleration.y = ACCELERATION;
			} else if (Gdx.input.isKeyPressed(Keys.S) || down) {
				acceleration.y = -ACCELERATION;
			} else {
				acceleration.y = 0;
			}

			if (touch0) {
				if (dpadRect.contains(x0, y0)) {
					float x = (x0 - 64) / 64;
					float y = (y0 - 64) / 64;
					float len = (float)Math.sqrt(x * x + y * y);
					if (len != 0) {
						x /= len;
						y /= len;
					} else {
						x = 0;
						y = 0;
					}
					vel.x = x * MAX_VELOCITY;
					vel.y = y * MAX_VELOCITY;
				} else {
					acceleration.x = 0;
					acceleration.y = 0;
				}
			}
		}
	}

	Rectangle[] r = {new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle()};

	/**
	 * try
	 */
	private void tryMove () {
		bounds.x += vel.x;
		fetchCollidableRects();
		for (int i = 0; i < r.length; i++) {
			Rectangle rect = r[i];
			if (bounds.overlaps(rect)) {
				if (vel.x < 0)
					bounds.x = rect.x + rect.width + 0.01f;
				else
					bounds.x = rect.x - bounds.width - 0.01f;
				vel.x = 0;
			}
		}

		bounds.y += vel.y;
		fetchCollidableRects();
		for (int i = 0; i < r.length; i++) {
			Rectangle rect = r[i];
			if (bounds.overlaps(rect)) {
				if (vel.y < 0) {
					bounds.y = rect.y + rect.height + 0.01f;
				} else
					bounds.y = rect.y - bounds.height - 0.01f;
				vel.y = 0;
			}
		}

		pos.x = bounds.x - 0.2f;
		pos.y = bounds.y - 0.2f;
	}

	//DEBUG HOTFIX
	/**
	 * Collision
	 */
	private void fetchCollidableRects () {
		int p1x = (int)bounds.x;
		int p1y = (int)Math.floor(bounds.y);
		int p2x = (int)(bounds.x + bounds.width);
		int p2y = (int)Math.floor(bounds.y);
		int p3x = (int)(bounds.x + bounds.width);
		int p3y = (int)(bounds.y + bounds.height);
		int p4x = (int)bounds.x;
		int p4y = (int)(bounds.y + bounds.height);

		int[][] tiles = map.tiles;
		int tile1 = tiles[p1x][map.tiles[0].length - 1 - p1y];
		int tile2 = tiles[p2x][map.tiles[0].length - 1 - p2y];
		int tile3 = tiles[p3x][map.tiles[0].length - 1 - p3y];
		int tile4 = tiles[p4x][map.tiles[0].length - 1 - p4y];

		if (tile1 != Map.EMPTY)
			r[0].set(p1x, p1y, 1, 1);
		else
			r[0].set(-1, -1, 0, 0);
		if (tile2 != Map.EMPTY)
			r[1].set(p2x, p2y, 1, 1);
		else
			r[1].set(-1, -1, 0, 0);
		if (tile3 != Map.EMPTY)
			r[2].set(p3x, p3y, 1, 1);
		else
			r[2].set(-1, -1, 0, 0);
		if (tile4 != Map.EMPTY)
			r[3].set(p4x, p4y, 1, 1);
		else
			r[3].set(-1, -1, 0, 0);
	}

	public void setControlled () {
		if (state == FOLLOW) {
			state = CONTROLLED;
			stateTime = 0;
		}
	}
}
