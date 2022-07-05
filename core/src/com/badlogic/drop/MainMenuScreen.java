package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

	final Drop game;
	OrthographicCamera camera;
	
	public MainMenuScreen(final Drop game) {
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		
		// update camera attributes
		camera.update();
		// set batch projection matrix to camera settings
		game.batch.setProjectionMatrix(camera.combined);
		// bulk batching
		game.batch.begin();
		game.font.draw(game.batch, "Welcome to Drop!", 100, 150); // text on screen
		game.font.draw(game.batch, "Press  SPACE to begin!", 100, 100); // text on screen
		game.batch.end();
		
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
