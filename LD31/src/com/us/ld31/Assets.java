package com.us.ld31;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {

	private final TextureAtlas atlas;
	
	public final TextureRegion uiTopBarBackground;
	public final TextureRegion uiStrIcon;
	public final TextureRegion uiDexIcon;
	public final TextureRegion uiIntIcon;
	public final TextureRegion uiBlock;
	public final TextureRegion uiArrowUp;
	public final TextureRegion uiArrowDown;
	public final TextureRegion uiMissing;
	public final TextureRegion uiHealthBar;
	public final TextureRegion uiExpBar;
	public final TextureRegion uiSkillbookIcon;
	public final TextureRegion uiSkillbookBg;
	public final BitmapFont fontBig;
	public final BitmapFont fontSmall;
	
	public final TextureRegion tileGrass;
	public final TextureRegion tileHouse;
	public final TextureRegion tileRoad;
	public final TextureRegion tileRock;
	public final TextureRegion tileTree;
	
	public final TextureRegion attackRanged;
	public final TextureRegion attackMelee;
	
	public Assets() {
		atlas = new TextureAtlas(Gdx.files.internal("textures/Game.pack"));
		
		uiBlock = atlas.findRegion("Block");
		uiArrowUp = atlas.findRegion("arrow");
		uiMissing = atlas.findRegion("missing");
		uiTopBarBackground = atlas.findRegion("ui");
		uiStrIcon = atlas.findRegion("str");
		uiDexIcon = atlas.findRegion("dex");
		uiIntIcon = atlas.findRegion("int");
		uiHealthBar = atlas.findRegion("hp");
		uiExpBar = atlas.findRegion("kazkas");
		uiSkillbookIcon = atlas.findRegion("skillicon");
		uiSkillbookBg = atlas.findRegion("skillbg");
		uiArrowDown = new TextureRegion(uiArrowUp);
		uiArrowDown.flip(false, true);

		uiBlock.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		tileGrass = atlas.findRegion("grass");
		tileHouse = atlas.findRegion("house");
		tileRoad = atlas.findRegion("road");
		tileRock = atlas.findRegion("rock");
		tileTree = atlas.findRegion("tree");

		fontBig = new BitmapFont(Gdx.files.internal("fonts/FontBig.fnt"), atlas.findRegion("FontBig"));
		fontSmall = new BitmapFont(Gdx.files.internal("fonts/FontSmall.fnt"), atlas.findRegion("FontSmall"));
		
		attackRanged = atlas.findRegion("RangedArrow");
		attackMelee = atlas.findRegion("Sword");
	}
	
	@Override
	public void dispose() {
		atlas.dispose();
		fontBig.dispose();
		fontSmall.dispose();
	}
	
}
