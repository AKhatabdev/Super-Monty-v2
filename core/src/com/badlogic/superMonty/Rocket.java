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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * ROCKET
 */
public class Rocket {
	Map map;
	static final int FLYING = 0;
	static final int EXPLODING = 1;
	static final int DEAD = 2;
	static final float VELOCITY = 6;
	float stateTime = 0;
	int state = FLYING;

	Vector2 startPos = new Vector2();
	Vector2 pos = new Vector2();
	Vector2 vel = new Vector2();
	Rectangle bounds = new Rectangle();
	//Sound effect Open Source:https://www.freesoundeffects.com/free-track/explosion-1-466446/
	//private Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("data/explosion_1.wav"));

	/**
	 * Rocket Map Pos
	 * @param map
	 * @param x
	 * @param y
	 */
	public Rocket (Map map, float x, float y) {
		this.map = map;
		this.startPos.set(x, y);
		this.pos.set(x, y);
		this.bounds.x = x + 0.2f;
		this.bounds.y = y + 0.2f;
		this.bounds.width = 0.6f;
		this.bounds.height = 0.6f;
		this.vel.set(-VELOCITY, 0);
	}

	/**
	 * if(pos.dst(map.monty.pos) < pos.dst(map.magicCube.pos)) vel.set(map.monty.pos);
	 * else vel.set(map.magicCube.pos);
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		//STATE FLYING
		if (state == FLYING) {
			vel.set(map.monty.position);
			vel.sub(pos).nor().scl(VELOCITY);
			pos.add(vel.x * deltaTime, vel.y * deltaTime);
			bounds.x = pos.x + 0.2f;
			bounds.y = pos.y + 0.2f;
			if (checkHit()) {
				state = EXPLODING;
				stateTime = 0;
			}
		}
		//STATE ROCKET EXPLOSION
		if (state == EXPLODING) {
			if (stateTime > 0.6f) {
				state = FLYING;
				stateTime = 0;
				pos.set(startPos);
				bounds.x = pos.x + 0.2f;
				bounds.y = pos.y + 0.2f;
			}
		}
		stateTime += deltaTime;
	}

	Rectangle[] r = {new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle()};

	/**
	 * HIT DETECTION
	 * @return TRUE
	 */
	private boolean checkHit () {
		fetchCollidableRects();
		for (int i = 0; i < r.length; i++) {
			if (bounds.overlaps(r[i])) {
				//TODO: work out how to pan the sound and pause when not in range
				//explosionSound.play(0.5f);
				return true;
			}
		}

		/**
 		* @Monty COLLISION DETECTION @ROCKETS
 		*/
		if (bounds.overlaps(map.monty.bounds)) {
			if (map.monty.state != com.badlogic.superMonty.Monty.DYING) {
				map.monty.state = Monty.DYING;
				map.monty.stateTime = 0;
				Gdx.input.vibrate(100);
			}
			return true;
		}

		return bounds.overlaps(map.magicCube.bounds);
	}

	/**
	 * SET BOUNDS MAP AND OBJECTS
	 * //DEBUG: FIXED(LOGICAL ERROR)
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
		//MAP TILES
		int[][] tiles = map.tiles;
		int tile1 = tiles[p1x][map.tiles[0].length - 1 - p1y];
		int tile2 = tiles[p2x][map.tiles[0].length - 1 - p2y];
		int tile3 = tiles[p3x][map.tiles[0].length - 1 - p3y];
		int tile4 = tiles[p4x][map.tiles[0].length - 1 - p4y];
		//Populate
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
}
