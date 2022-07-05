package com.badlogic.drop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
	final Drop game;
	
	Texture dropImage;
	Texture bucketImage;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	long lastDropTime;
	int dropsGathered;
	
	public GameScreen(final Drop game) {
		this.game = game;
		
		// load images for drop and bucket (64x64)
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		
		// load drop sound and rain music (remember to set  music loop)
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		rainMusic.setLooping(true);
		
		// create camera and sprite batch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		// create rectangle to represent bucket
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		
		bucket.width = 64;
		bucket.height = 64;
		
		// create raindrops array and spawn first drop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}
	
	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
	
	@Override
	public void show() {
		// start/resume playback of music when screen is shown again
		rainMusic.play();
	}

	@Override
	public void render(float delta) {
		// clear entire screen with light blue color
		ScreenUtils.clear(0, 0, 0.4f, 1);
		
		// update camera matrices
		// good practice to update for each render(?)
		camera.update();
		
		// tell SpriteBatch to render in camera coordinate system
		game.batch.setProjectionMatrix(camera.combined);
		
		// being bulk batch, draw bucket and all raindrops in Array
		game.batch.begin();
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
		game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		game.batch.end();
		
		// bucket left and right arrow keys
		if(Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 200 * Gdx.graphics.getDeltaTime();
		// make sure bucket stays within screen bounds
		if(bucket.x < 0)
			bucket.x = 0;
		if(bucket.y > 800 - 64)
			bucket.y = 800 - 64;
		
		// generate new raindrop every X seconds
		if((TimeUtils.nanoTime() - lastDropTime) * 1E-9 > 0.5)
			spawnRaindrop();
		
		/*move raindrops downwards, delete drops if they reach bottom or
		 * if caught in bucket. add points if caught and play noise.*/
		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()) {
			Rectangle raindrop = (Rectangle) iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y < 0)
				iter.remove();
			if(raindrop.overlaps(bucket)) {
				dropsGathered++;
				dropSound.play();
				iter.remove();
			}
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
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}

}
