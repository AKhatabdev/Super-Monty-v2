/*
  Title:
  Super Monty
  ©2020 Awais Khatab

  Target Platform: Android 7.0
  Module: CI360

  Copyright 2020, Awais Khatab, All rights reserved.

 */
package com.badlogic.superMonty;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * CREATE Monty Lives Logic
 */

public class Lives {
    com.badlogic.superMonty.Map map;
    //TEXTURES
    SpriteBatch batch;

    TextureRegion heart_1;
    TextureRegion heart_2;
    TextureRegion heart_3;
    TextureRegion heart_4;
    TextureRegion heart_5;

    TextureRegion empty_heart_1;
    TextureRegion empty_heart_2;
    TextureRegion empty_heart_3;
    TextureRegion empty_heart_4;
    TextureRegion empty_heart_5;

    //Map -> Load
    public Lives(Map map) {
        this.map = map;
        loadAssets();
    }

    /**
     * LOAD ASSETS
     */
    public void loadAssets(){
        Texture HEARTS = new Texture(Gdx.files.internal("data/hearts.png"));
        TextureRegion[] hearts = TextureRegion.split(HEARTS, 20, 20)[0];
        heart_1 = hearts[0];
        heart_2 = hearts[1];
        heart_3 = hearts[2];
        heart_4 = hearts[3];
        heart_5 = hearts[4];

        Texture EMPTY_HEARTS = new Texture(Gdx.files.internal("data/emptyHearts.png"));
        TextureRegion[] emptyHearts = TextureRegion.split(EMPTY_HEARTS, 20, 20)[0];
        empty_heart_1 = emptyHearts[0];
        empty_heart_2 = emptyHearts[1];
        empty_heart_3 = emptyHearts[2];
        empty_heart_4 = emptyHearts[3];
        empty_heart_5 = emptyHearts[4];

        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);

    }

    /**
     * RENDER ASSETS
     */
    //Y axis pos
    private final int posY = 32;

    /**
     * RENDER HEARTS
     */
    public void render() {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {// IF Lives = 3 Render 3 Hearts
            int montyLives = com.badlogic.superMonty.Monty.getLIVES();

            if (montyLives == 6) {
                batch.begin();
                batch.draw(heart_1, 5, 320 - posY);
                batch.draw(heart_2, 25, 320 - posY);
                batch.draw(heart_3, 45, 320 - posY);
                batch.draw(heart_4, 65, 320 - posY);
                batch.draw(heart_5, 85, 320 - posY);
                batch.end();
            }
           else if (montyLives == 5) {
                batch.begin();
                batch.draw(heart_1, 5, 320 - posY);
                batch.draw(heart_2, 25, 320 - posY);
                batch.draw(heart_3, 45, 320 - posY);
                batch.draw(heart_4, 65, 320 - posY);
                batch.draw(empty_heart_5, 85, 320 - posY);
                batch.end();
            }
            else if (montyLives == 4) {
                batch.begin();
                batch.draw(heart_1, 5, 320 - posY);
                batch.draw(heart_2, 25, 320 - posY);
                batch.draw(heart_3, 45, 320 - posY);
                batch.draw(empty_heart_4, 65, 320 - posY);
                batch.draw(empty_heart_5, 85, 320 - posY);
                batch.end();
            }
            else if (montyLives == 3) {
                batch.begin();
                batch.draw(heart_1, 5, 320 - posY);
                batch.draw(heart_2, 25, 320 - posY);
                batch.draw(empty_heart_3, 45, 320 - posY);
                batch.draw(empty_heart_4, 65, 320 - posY);
                batch.draw(empty_heart_5, 85, 320 - posY);
                batch.end();
            }
            else if (montyLives == 2) {
                batch.begin();
                batch.draw(heart_1, 5, 320 - posY);
                batch.draw(empty_heart_2, 25, 320 - posY);
                batch.draw(empty_heart_3, 45, 320 - posY);
                batch.draw(empty_heart_4, 65, 320 - posY);
                batch.draw(empty_heart_5, 85, 320 - posY);
                batch.end();
            }
            else if (montyLives == 1) {
                batch.begin();
                batch.draw(empty_heart_1, 5, 320 - posY);
                batch.draw(empty_heart_2, 25, 320 - posY);
                batch.draw(empty_heart_3, 45, 320 - posY);
                batch.draw(empty_heart_4, 65, 320 - posY);
                batch.draw(empty_heart_5, 85, 320 - posY);
                batch.end();
            }
            //IF Lives = 2 Render 2 Hearts
        }
    }

    /**
     * DISPOSE ASSETS
     */
    public void dispose(){
        batch.dispose();
    }

}
