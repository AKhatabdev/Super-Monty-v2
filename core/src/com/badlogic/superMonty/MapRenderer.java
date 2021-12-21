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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector3;

/**
 * RENDER MAP
 *
 */
public class MapRenderer {
	private final Map map;
	private final OrthographicCamera cam;
	//Sprite
	private final SpriteCache cache;
	private final SpriteBatch batch = new SpriteBatch(5460);

	private final ImmediateModeRenderer20 renderer = new ImmediateModeRenderer20(false, true, 0);
	private final int[][] blocks;
	//TILE Image Texture
	private TextureRegion tile;
	//MONTY Image Textures
	private Animation<TextureRegion> montyLeft;
	private Animation<TextureRegion> montyRight;
	private Animation<TextureRegion> montyJumpLeft;
	private Animation<TextureRegion> montyJumpRight;
	private Animation<TextureRegion> montyIdleLeft;
	private Animation<TextureRegion> montyIdleRight;
	public Animation<TextureRegion> montyDead;
	//MAGIC CUBE Image Textures
	private TextureRegion magicCube;
	private Animation<TextureRegion> cubeFixed;
	private TextureRegion magicCubeControlled;
	private TextureRegion saveSpot;
	private Animation<TextureRegion> spawn;
	private Animation<TextureRegion> dying;
	private TextureRegion spikes;
	private Animation<TextureRegion> rocket;
	private Animation<TextureRegion> rocketExplosion;
	private TextureRegion rocketPad;
	private TextureRegion exitDoor;
	private TextureRegion movingSpikes;
	private TextureRegion water;

	private final FPSLogger fps = new FPSLogger();

	/**
	 * LIBGDX MAP RENDERING @ MAP @CAM
	 *
	 * @param map
	 */
	public MapRenderer(Map map) {
		this.map = map;
		this.cam = new OrthographicCamera(24, 16);
		this.cam.position.set(map.monty.position.x, map.monty.position.y, 0);
		this.cache = new SpriteCache(this.map.tiles.length * this.map.tiles[0].length, false);
		this.blocks = new int[(int) Math.ceil(this.map.tiles.length / 24.0f)][(int) Math.ceil(this.map.tiles[0].length / 16.0f)];

		try {
			createAnimations();
			createBlocks();
		} catch (Exception e) {
			System.out.println("Error Failed to create Animations and Blocks @MapRenderer.java");
			Gdx.app.debug("Error", "Failed to create Blocks and Animations");
		}
	}

	/**
	 *
	 */
	private void createBlocks() {
		int width = map.tiles.length;
		int height = map.tiles[0].length;
		for (int blockY = 0; blockY < blocks[0].length; blockY++) {
			for (int blockX = 0; blockX < blocks.length; blockX++) {
				cache.beginCache();
				for (int y = blockY * 16; y < blockY * 16 + 16; y++) {
					for (int x = blockX * 24; x < blockX * 24 + 24; x++) {
						if (x > width) continue;
						if (y > height) continue;
						int posX = x;
						int posY = height - y - 1;
						if (map.match(map.tiles[x][y], Map.TILE)) cache.add(tile, posX, posY, 1, 1);
						if (map.match(map.tiles[x][y], Map.WATER))
							cache.add(spikes, posX, posY, 1, 1);
					}
				}
				blocks[blockX][blockY] = cache.endCache();
			}
		}
		Gdx.app.debug("Super Monty", "blocks created");
	}

	/**
	 * CREATE ANIMATIONS
	 *
	 * @Ignore Warning @Animation()
	 */
	private void createAnimations() {
		//SWITCH method for Level Theme Selection @GameScreen input 1-3
		int levelSelect = com.badlogic.superMonty.screens.GameScreen.getLevelSelector();
		String asset;
		//Level Selection
		switch(levelSelect){
			case 1:
				asset = ("data/sprite_assets/assets_level_1.png");
				this.tile = new TextureRegion(new Texture(Gdx.files.internal("data/tile.png")), 0, 0, 20, 20);
				break;
			case 2:
				asset = ("data/sprite_assets/assets_level_2.png");
				this.tile = new TextureRegion(new Texture(Gdx.files.internal("data/tile_ice.png")), 0, 0, 20, 20);
				break;
			case 3:
				asset = ("data/sprite_assets/assets_level_3.png");
				this.tile = new TextureRegion(new Texture(Gdx.files.internal("data/tile_lavarock.png")), 0, 0, 20, 20);
				break;
			default:
				throw new IllegalStateException("Asset Data Error " + levelSelect);
		}

		//ANIM ASSIGNMENT
		Texture montyTexture = new Texture(Gdx.files.internal(asset));

		TextureRegion[] split = new TextureRegion(montyTexture).split(20, 20)[0];
		TextureRegion[] mirror = new TextureRegion(montyTexture).split(20, 20)[0];
		for (TextureRegion region : mirror)
			region.flip(true, false);
		spikes = split[5];
		montyRight = new Animation(0.1f, split[0], split[1]);
		montyLeft = new Animation(0.1f, mirror[0], mirror[1]);
		montyJumpRight = new Animation(0.1f, split[2], split[3]);
		montyJumpLeft = new Animation(0.1f, mirror[2], mirror[3]);
		montyIdleRight = new Animation(0.5f, split[0], split[4]);
		montyIdleLeft = new Animation(0.5f, mirror[0], mirror[4]);
		montyDead = new Animation(0.2f, split[0]);
		split = new TextureRegion(montyTexture).split(20, 20)[1];
		magicCube = split[0];
		cubeFixed = new Animation(1, split[1], split[2], split[3], split[4], split[5]);
		split = new TextureRegion(montyTexture).split(20, 20)[2];
		magicCubeControlled = split[0];
		spawn = new Animation(0.1f, split[4], split[3], split[2], split[1]);
		dying = new Animation(0.1f, split[1], split[2], split[3], split[4]);
		saveSpot = split[5];
		split = new TextureRegion(montyTexture).split(20, 20)[3];
		rocket = new Animation(0.1f, split[0], split[1], split[2], split[3]);
		rocketPad = split[4];
		split = new TextureRegion(montyTexture).split(20, 20)[4];
		rocketExplosion = new Animation(0.1f, split[0], split[1], split[2], split[3], split[4], split[4]);
		split = new TextureRegion(montyTexture).split(20, 20)[5];
		exitDoor = split[2];
		movingSpikes = split[0];
		water = split[1];
	}

	float stateTime = 0;
	Vector3 lerpTarget = new Vector3();

	/**
	 * @param deltaTime
	 */
	public void render(float deltaTime) {
		if (map.magicCube.state != MagicCube.CONTROLLED)
			cam.position.lerp(lerpTarget.set(map.monty.position.x, map.monty.position.y, 0), 2f * deltaTime);
		else
			cam.position.lerp(lerpTarget.set(map.magicCube.pos.x, map.magicCube.pos.y, 0), 2f * deltaTime);
		cam.update();
		renderWaterBeams();
		cache.setProjectionMatrix(cam.combined);
		Gdx.gl.glDisable(GL20.GL_BLEND);
		cache.begin();
		int b = 0;
		for (int blockY = 0; blockY < 4; blockY++) {
			for (int blockX = 0; blockX < 6; blockX++) {
				cache.draw(blocks[blockX][blockY]);
				b++;
			}
		}
		cache.end();
		stateTime += deltaTime;
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		renderSaveSpot();

		if (map.exitDoor != null)
			batch.draw(exitDoor, map.exitDoor.bounds.x, map.exitDoor.bounds.y, 1, 1);
		renderWaterLasers();
		renderMovingSpikes();
		renderMonty();

		renderMagicCube();
		renderRockets();
		batch.end();
		renderWaterBeams();

		//DEBUG FPS
		fps.log();
	}

	/**
	 * RENDER MAIN CHARACTER
	 */
	private void renderMonty() {
		Animation<TextureRegion> anim = null;
		boolean loop = true;
		try {
			if (map.monty.state == Monty.RUN) {
				if (map.monty.dir == Monty.LEFT)
					anim = montyLeft;
				else
					anim = montyRight;
			}
			if (map.monty.state == Monty.IDLE) {
				if (map.monty.dir == Monty.LEFT)
					anim = montyIdleLeft;
				else
					anim = montyIdleRight;
			}
			if (map.monty.state == Monty.JUMP) {
				if (map.monty.dir == Monty.LEFT)
					anim = montyJumpLeft;
				else
					anim = montyJumpRight;
			}
			if (map.monty.state == Monty.SPAWN) {
				anim = spawn;
				loop = false;
			}
			if (map.monty.state == Monty.DYING) {
				anim = dying;
				loop = false;
			}
			batch.draw(anim.getKeyFrame(map.monty.stateTime, loop), map.monty.position.x, map.monty.position.y, 1, 1);
		} catch(Exception e) {
			System.out.println("Error renderMonty @MapRenderer.java");
			Gdx.app.debug("Error", "Failed renderMonty");
		}
	}


	/**
	 * RENDER MAGICAL CUBE
	 * TODO: @renderMagicCube *FIXED*
	 */
	private void renderMagicCube() {
		try{
			if (map.magicCube.state == MagicCube.FOLLOW) batch.draw(magicCube, map.magicCube.pos.x, map.magicCube.pos.y, 1.5f, 1.5f);
			if (map.magicCube.state == MagicCube.FIXED)
				batch.draw(cubeFixed.getKeyFrame(map.magicCube.stateTime, false), map.magicCube.pos.x, map.magicCube.pos.y, 1.5f, 1.5f);
			if (map.magicCube.state == MagicCube.CONTROLLED) batch.draw(magicCubeControlled, map.magicCube.pos.x, map.magicCube.pos.y, 1.5f, 1.5f);
		} catch(Exception e){
			System.out.println("Error renderMagicCube @MapRenderer.java");
			Gdx.app.debug("Error", "Failed renderMagicCube");
		}

	}

	/**
	 * RENDER ROCKETS
	 */
	private void renderRockets () {
		try{
			for (int i = 0; i < map.rockets.size; i++) {
				Rocket rocket = map.rockets.get(i);
				if (rocket.state == Rocket.FLYING) {
					TextureRegion frame = this.rocket.getKeyFrame(rocket.stateTime, true);
					batch.draw(frame, rocket.pos.x, rocket.pos.y, 0.5f, 0.5f, 1, 1, 1, 1, rocket.vel.angle());
				} else {
					TextureRegion frame = this.rocketExplosion.getKeyFrame(rocket.stateTime, false);
					batch.draw(frame, rocket.pos.x, rocket.pos.y, 1, 1);
				}
				batch.draw(rocketPad, rocket.startPos.x, rocket.startPos.y, 1, 1);
			}
		} catch(Exception e){
			System.out.println("Error renderRockets @MapRenderer.java");
			Gdx.app.debug("Error", "Failed renderRockets");
		}

	}

	/**
	 * RENDER SAVE SPOT
	 */
	private void renderSaveSpot () {
		try{
			for (int i = 0; i < map.saveSpots.size; i++) {
				SaveSpot saveSpot = map.saveSpots.get(i);
				batch.draw(this.saveSpot, saveSpot.bounds.x, saveSpot.bounds.y, 1, 1);
			}
		} catch(Exception e){
			System.out.println("Error renderSaveSpot @MapRenderer.java");
			Gdx.app.debug("Error", "Failed renderSaveSpot");
		}

	}

	/**
	 * RENDER SPIKES
	 */
	private void renderMovingSpikes () {
		try{
			for (int i = 0; i < map.movingSpikes.size; i++) {
				MovingSpikes spikes = map.movingSpikes.get(i);
				batch.draw(movingSpikes, spikes.pos.x, spikes.pos.y, 0.5f, 0.5f, 1, 1, 1, 1, spikes.angle);
			}
		} catch(Exception e){
			System.out.println("Error renderMovingSpikes @MapRenderer.java");
			Gdx.app.debug("Error", "Failed renderMovingSpikes");
		}

	}

	/**
	 * RENDER WATER LASERS BASES
	 */
	private void renderWaterLasers() {
		try{
			for (int i = 0; i < map.waterLasers.size; i++) {
				WaterLaser laser = map.waterLasers.get(i);
				batch.draw(this.water, laser.pos.x, laser.pos.y, 0.5f, 0.5f, 1, 1, 1, 1, laser.angle);
			}
		} catch(Exception e){
			System.out.println("Error renderWaterLasers @MapRenderer.java");
			Gdx.app.debug("Error", "Failed renderWaterLasers");
		}

	}

	/**
	 * Render WATERBEAMS
	 * (color) beam colour
	 */
	private void renderWaterBeams() {
		try{
			cam.update(false);
			renderer.begin(cam.combined, GL20.GL_LINES);
			for (int i = 0; i < map.waterLasers.size; i++) {
				WaterLaser laser = map.waterLasers.get(i);
				float sx = laser.startPoint.x, sy = laser.startPoint.y;
				float ex = laser.cappedEndPoint.x, ey = laser.cappedEndPoint.y;
				renderer.color(Color.SKY);
				renderer.vertex(sx, sy, 0);
				renderer.color(Color.SKY);
				renderer.vertex(ex, ey, 0);
			}
			renderer.end();
		} catch(Exception e){
			System.out.println("Error renderWaterBeams @MapRenderer.java");
			Gdx.app.debug("Error", "Failed renderWaterBeams");
		}
	}

	/**
	 * DISPOSE
	 */
	public void dispose () {
		cache.dispose();
		batch.dispose();
		tile.getTexture().dispose();
		magicCube.getTexture().dispose();
	}
}
