package com.badlogic.superMonty.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.superMonty.GifDecoder;

/**
 * INTRO
 * Add animated Intro *COMPLETED*
 *
 */

public class Level1IntroScreen extends SuperMontyScreen {
    private final Animation<TextureRegion> animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("data/level_intros/intro_level_1.gif").read());
    SpriteBatch batch;
    float time = 0;
    float elapsed;
    private final Sound selectSound = Gdx.audio.newSound(Gdx.files.internal("data/sound_FX/select_sound.wav"));
    /**
     * LEVEL 1 INTRO SCREEN
     * @param game
     */
    public Level1IntroScreen (Game game) {
        super(game);
    }

    @Override
    public void show () {
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
    }

    @Override
    public void render (float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(animation.getKeyFrame(elapsed), 20.0f, 20.0f);
        batch.end();

        time += delta;
        if (time > 2) {
                selectSound.play(1.0f);
                Gdx.input.vibrate(50);
                game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void hide () {
        Gdx.app.debug("Super Monty", "dispose Level intro");
        selectSound.dispose();
        batch.dispose();
    }

}