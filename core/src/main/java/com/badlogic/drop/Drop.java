package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.List;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Drop extends ApplicationAdapter {
    private Texture dropImg;
    private Texture bucketImg;
    private Texture dropImg2;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private com.badlogic.gdx.math.Rectangle bucket;
    private Array<com.badlogic.gdx.math.Rectangle> raindrops;
    private long lastDropTime;
    private Music setMusic;
    private Array<Sound> soundList;
    private Sound soundDrop;
    private Sound soundDrop2;
    private Sound soundDrop3;
    private Sound soundDrop4;
    private Sound soundDrop5;
    private Sound soundDrop6;
    private Array<Sound> soundArray;
    public Drop() {
    }

    @Override
    public void create() {
        dropImg = new Texture(Gdx.files.internal("C:\\Users\\ferna\\Documents\\TRABALHO-ESTUDO\\game tutorial\\assets\\drop.png"));
        dropImg2 = new Texture(Gdx.files.internal("drop.png"));
        bucketImg = new Texture(Gdx.files.internal("C:\\Users\\ferna\\Documents\\TRABALHO-ESTUDO\\game tutorial\\assets\\bucket.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        setMusic = Gdx.audio.newMusic(Gdx.files.internal("C:\\Users\\ferna\\Documents\\TRABALHO-ESTUDO\\game tutorial\\assets\\background.mp3"));
        soundDrop = Gdx.audio.newSound(Gdx.files.internal("C:\\Users\\ferna\\Documents\\TRABALHO-ESTUDO\\game tutorial\\assets\\drop\\drop1.wav"));
        soundDrop2 = Gdx.audio.newSound(Gdx.files.internal("C:\\Users\\ferna\\Documents\\TRABALHO-ESTUDO\\game tutorial\\assets\\drop\\drop2.wav"));
        soundDrop3 = Gdx.audio.newSound(Gdx.files.internal("C:\\Users\\ferna\\Documents\\TRABALHO-ESTUDO\\game tutorial\\assets\\drop\\drop3.wav"));
        soundDrop4 = Gdx.audio.newSound(Gdx.files.internal("C:\\Users\\ferna\\Documents\\TRABALHO-ESTUDO\\game tutorial\\assets\\drop\\drop4.wav"));
        soundDrop5 = Gdx.audio.newSound(Gdx.files.internal("C:\\Users\\ferna\\Documents\\TRABALHO-ESTUDO\\game tutorial\\assets\\drop\\drop5.wav"));
        soundDrop6 = Gdx.audio.newSound(Gdx.files.internal("C:\\Users\\ferna\\Documents\\TRABALHO-ESTUDO\\game tutorial\\assets\\drop\\drop6.wav"));
        soundList = new Array<>();
        soundArray = new Array<>();
        soundList.add(soundDrop, soundDrop2, soundDrop3, soundDrop4);
        soundArray.addAll(soundList);
        soundArray.add(soundDrop5, soundDrop6);


        //hitbox
        bucket = new Rectangle(800 / 2 - 64 / 2, 20, 64, 64);

        //random raindrop
        raindrops = new Array<>();
        spawnDrop();
    }

    @Override
    public void render() {
        //set camera
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();

        //set music loop
        setMusic.setLooping(true);
        setMusic.play();

        //set hitbox
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bucketImg, bucket.x, bucket.y);
        for (com.badlogic.gdx.math.Rectangle raindrop : raindrops) {
            batch.draw(dropImg2, raindrop.x, raindrop.y);
        }
        batch.end();


        //active game
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = (int) touchPos.x - 64 / 2;
        }

        //controlls
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) bucket.x -= 500 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) bucket.x += 500 * Gdx.graphics.getDeltaTime();

        //position spawn and bucket limit
        if (bucket.x < 0) bucket.x = 0;
        if (bucket.y > 800 - 40) bucket.y = 800 - 40;
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnDrop();

        for (Iterator<com.badlogic.gdx.math.Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
            com.badlogic.gdx.math.Rectangle raindrop = iter.next();
            System.out.println(soundArray.size);
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) iter.remove();
            if (raindrop.overlaps(bucket)) {
                Sound sound = soundArray.get(MathUtils.random(0,5));
                sound.play();
                iter.remove();
            }
        }


    }

    private void spawnDrop() {
        com.badlogic.gdx.math.Rectangle raindrop = new com.badlogic.gdx.math.Rectangle(MathUtils.random(0, 800 - 64), 480, 64, 64);
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void dispose() {
        dropImg.dispose();
        dropImg2.dispose();
        bucketImg.dispose();
        batch.dispose();
    }
}
