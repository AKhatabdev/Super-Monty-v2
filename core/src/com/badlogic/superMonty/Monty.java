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
 * CLASS: Monty
 * Game Object: Main Character Monty
 *
 */
public class Monty {
	//HOTFIX: MADE VAR PUBLIC @IGNORE WARNING FOR PRIVATE
	//VAR INT Values
	static final int IDLE = 0;
	static final int RUN = 1;
	static final int JUMP = 2;
	static final int SPAWN = 3;
	static final int DYING = 4;
	static final int DEAD = 5;
	static final int LEFT = -1;
	static final int RIGHT = 1;
	//VAR FLOAT Physics Values
	static final float ACCELERATION = 20f;
	static final float JUMP_VELOCITY = 10;
	static final float GRAVITY = 20.0f;
	static final float MAX_VEL = 6f;
	static final float DAMP = 0.90f;
	//Monty Starting Lives
	private static int LIVES = 6;

	Vector2 position = new Vector2();
	Vector2 acceleration = new Vector2();
	Vector2 velocity = new Vector2();

	//SOUND EFFECT
	private final Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/sound_FX/monty_jump.wav"));
	private final Sound DeathSound = Gdx.audio.newSound(Gdx.files.internal("data/sound_FX/monty_death.wav"));

	public Rectangle bounds = new Rectangle();

	int state = SPAWN;

	float stateTime = 0;
	int dir = LEFT;
	Map map;
	boolean grounded = false;

	/**
	 * MONTY POS MAP
	 * @param map
	 * @param x
	 * @param y
	 */
	public Monty(Map map, float x, float y) {
		this.map = map;
		position.x = x;
		position.y = y;
		bounds.width = 0.6f;
		bounds.height = 0.8f;
		bounds.x = position.x + 0.2f;
		bounds.y = position.y;
		state = SPAWN;
		stateTime = 0;
	}
	//Monty get(LIVES)
	public static int getLIVES(){
		return LIVES;
	}
	//Monty set(LIVES)
	public static void setLIVES(int resetLife){
		Monty.LIVES = resetLife;
	}

	/**
	 * UPDATE
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		processKeys();

		//Monty Physics
		acceleration.y = -GRAVITY;
		acceleration.scl(deltaTime);
		velocity.add(acceleration.x, acceleration.y);
		if (acceleration.x == 0) velocity.x *= DAMP;
		if (velocity.x > MAX_VEL) velocity.x = MAX_VEL;
		if (velocity.x < -MAX_VEL) velocity.x = -MAX_VEL;
		velocity.scl(deltaTime);
		tryMove();
		velocity.scl(1.0f / deltaTime);
		if (state == SPAWN) {
			if (stateTime > 0.4f) {
				state = IDLE;
			}
		}
		if (state == DYING) {
			if (stateTime > 0.4f) {
				DeathSound.play(1.0f);
				Gdx.input.vibrate(50);
				//Life counter
				LIVES -= 1;
				state = DEAD;
			}
		}

		stateTime += deltaTime;
	}
	/**
	 * USER INPUTS PROCESS
	 * Touch screen inputs and Keyboard inputs(For Testing)
	 */
	private void processKeys () {
		if (map.magicCube.state == MagicCube.CONTROLLED || state == SPAWN || state == DYING) return;
		float x0 = (Gdx.input.getX(0) / (float)Gdx.graphics.getWidth()) * 480;
		float x1 = (Gdx.input.getX(1) / (float)Gdx.graphics.getWidth()) * 480;
		float y0 = 320 - (Gdx.input.getY(0) / (float)Gdx.graphics.getHeight()) * 320;

		boolean leftButton = (Gdx.input.isTouched(0) && x0 < 70) || (Gdx.input.isTouched(1) && x1 < 70);
		boolean rightButton = (Gdx.input.isTouched(0) && x0 > 70 && x0 < 134) || (Gdx.input.isTouched(1) && x1 > 70 && x1 < 134);
		boolean jumpButton = (Gdx.input.isTouched(0) && x0 > 416 && x0 < 480 && y0 < 64)
			|| (Gdx.input.isTouched(1) && x1 > 416 && x1 < 480 && y0 < 64);

		if ((Gdx.input.isKeyPressed(Keys.W) || jumpButton) && state != JUMP) {
			state = JUMP;
			velocity.y = JUMP_VELOCITY;
			grounded = false;
			//JUMP SOUND
			jumpSound.play(1.0f);
			//Vibration JUMP
			Gdx.input.vibrate(100);
		}
		if (Gdx.input.isKeyPressed(Keys.A) || leftButton) {
			if (state != JUMP) state = RUN;
			dir = LEFT;
			acceleration.x = ACCELERATION * dir;
		} else if (Gdx.input.isKeyPressed(Keys.D) || rightButton) {
			if (state != JUMP) state = RUN;
			dir = RIGHT;
			acceleration.x = ACCELERATION * dir;
		} else {
			if (state != JUMP) state = IDLE;
			acceleration.x = 0;
		}
	}
	Rectangle[] r = {new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle()};

	/**
	 * TRY MOVE
	 * *FIXED*
	 */
	private void tryMove () {
		bounds.x += velocity.x;
		fetchCollidableRects();
		for (int i = 0; i < r.length; i++) {
			Rectangle rect = r[i];
			if (bounds.overlaps(rect)) {
				if (velocity.x < 0)
					bounds.x = rect.x + rect.width + 0.01f;
				else
					bounds.x = rect.x - bounds.width - 0.01f;
				velocity.x = 0;
			}
		}
		//DEBUG Physics
		bounds.y += velocity.y;
		fetchCollidableRects();
		for (int i = 0; i < r.length; i++) {
			Rectangle rect = r[i];
			if (bounds.overlaps(rect)) {
				if (velocity.y < 0) {
					bounds.y = rect.y + rect.height + 0.01f;
					grounded = true;
					if (state != DYING && state != SPAWN) state = Math.abs(acceleration.x) > 0.1f ? RUN : IDLE;
				} else
					bounds.y = rect.y - bounds.height - 0.01f;
				velocity.y = 0;
			}
		}
		position.x = bounds.x - 0.2f;
		position.y = bounds.y;
	}

	/**
	 * LOGIC COLLISION RECT
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

		//Tiles Mapped
		int[][] tiles = map.tiles;
		int tile1 = tiles[p1x][map.tiles[0].length - 1 - p1y];
		int tile2 = tiles[p2x][map.tiles[0].length - 1 - p2y];
		int tile3 = tiles[p3x][map.tiles[0].length - 1 - p3y];
		int tile4 = tiles[p4x][map.tiles[0].length - 1 - p4y];

		if (state != DYING && (map.isDeadly(tile1) || map.isDeadly(tile2) || map.isDeadly(tile3) || map.isDeadly(tile4))) {
			state = DYING;
			stateTime = 0;
		}
		//DEBUG IF ELSE
		if (tile1 == Map.TILE)
			r[0].set(p1x, p1y, 1, 1);
		else
			r[0].set(-1, -1, 0, 0);
		if (tile2 == Map.TILE)
			r[1].set(p2x, p2y, 1, 1);
		else
			r[1].set(-1, -1, 0, 0);
		if (tile3 == Map.TILE)
			r[2].set(p3x, p3y, 1, 1);
		else
			r[2].set(-1, -1, 0, 0);
		if (tile4 == Map.TILE)
			r[3].set(p4x, p4y, 1, 1);
		else
			r[3].set(-1, -1, 0, 0);

		//@Monty SET BOUNDS @MagicCube
		if (map.magicCube.state == MagicCube.FIXED) {
			r[4].x = map.magicCube.bounds.x;
			r[4].y = map.magicCube.bounds.y;
			r[4].width = map.magicCube.bounds.width;
			r[4].height = map.magicCube.bounds.height;
		} else
			r[4].set(-1, -1, 0, 0);
	}
}
