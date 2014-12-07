package com.us.ld31.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.us.ld31.game.GameWorld;
import com.us.ld31.game.skills.Skill;
import com.us.ld31.game.skills.SkillInfo;
import com.us.ld31.game.skills.SkillState;
import com.us.ld31.utils.SpriteActor;
import com.us.ld31.utils.TouchListener;
import com.us.ld31.utils.steps.Steps;
import com.us.ld31.utils.steps.scene.ActorSteps;

public class SkillBar extends Group {

	public static class SkillButton extends Group {
		private final SpriteActor border = new SpriteActor();
		private final SpriteActor icon = new SpriteActor();
		private final SpriteActor cooldownOverlay = new SpriteActor();
		private final Label mapping;
		private SkillState state;
		
		private boolean shrinked = true;
		
		public SkillButton(final SkillBar bar, 
						   final GameUi gameUi, 
						   final int mappingCode) {
			
			border.setRegion(gameUi.getApp().assets.uiBlock);
			border.setColor(Color.BLACK);
			icon.setRegion(gameUi.getApp().assets.uiMissing);
	
			cooldownOverlay.setRegion(gameUi.getApp().assets.uiBlock);
			cooldownOverlay.setColor(Color.RED);
			cooldownOverlay.getColor().a = 0f;
			
			mapping = new Label("", new LabelStyle(gameUi.getApp().assets.fontBig, new Color(1f, 1f, 1f, 1f)));
			
			addActor(border);
			addActor(icon);
			addActor(cooldownOverlay);
			addActor(mapping);
			
			icon.setSize(48, 48);
			border.setSize(58, 58);
			mapping.setPosition(border.getWidth() - icon.getWidth(), border.getHeight() - icon.getHeight());
			
			setSize(border.getWidth(), border.getHeight());
			icon.setPosition(getWidth() / 2f - icon.getWidth() / 2f, getHeight() / 2f - icon.getHeight() / 2f);
			cooldownOverlay.setPosition(icon.getX(), icon.getY());
			cooldownOverlay.setSize(icon.getWidth(), icon.getHeight());
			
			setOrigin(0f, 0f);
			
			setScale(0.6f, 0.6f);
			
			mapping.setText("" + mappingCode);
			mapping.pack();
			
			addListener(new TouchListener() {
				@Override
				public void touched() {
					bar.changeActiveButton(SkillButton.this);
				}
			});
		}
		
		@Override
		public void act(final float delta) {
			super.act(delta);
			
			if(state != null) {
				final float totalCooldown = state.getCooldown();
				
				if(totalCooldown > 0f) {
					if(state.updateCooldown(delta)) {
						state.setCooldown(0f);
						cooldownOverlay.setHeight(icon.getHeight());
						cooldownOverlay.clearActions();
						cooldownOverlay.setColor(Color.GREEN);
						cooldownOverlay.getColor().a = 0f;
						cooldownOverlay.addAction(
								Steps.action(
										Steps.sequence(
												ActorSteps.alphaTo(0.7f, 0.5f, Interpolation.circleOut),
												ActorSteps.alphaTo(0f, 0.5f))));
					}
					else {
						cooldownOverlay.setColor(Color.BLACK);
						cooldownOverlay.getColor().a = 0.5f;
						cooldownOverlay.setHeight(icon.getHeight() * (state.getCooldownLeft() / totalCooldown));
					}
				}
			}
		}

		public void setState(final SkillState state) {
			this.state = state;
			if(state != null) {
				final float iconSize = icon.getWidth();
				icon.setRegion(state.getSkillInfo().icon);
				icon.setSize(iconSize, iconSize);
			}
		}
		
		public SkillState getState() {
			return state;
		}
		
		@Override
		public void setScale(final float scaleX, final float scaleY) {
			super.setScale(scaleX, scaleY);
			mapping.setFontScale(1f + 1f - scaleX);
		}
		
		public boolean isShrinked() {
			return shrinked;
		}
		
		public void grow() {
			clearActions();
			addAction(
					Steps.action(
							Steps.sequence(
									ActorSteps.scaleTo(1f, 1f, 0.3f, Interpolation.circleOut))));
		}
		
		public void shrink() {
			clearActions();
			addAction(
					Steps.action(
							Steps.sequence(
									ActorSteps.scaleTo(0.6f, 0.6f, 0.3f, Interpolation.circleOut))));
		}
	}
	
	private final GameUi gameUi;
	private final SpriteActor holder = new SpriteActor();
	private final Array<SkillButton> buttons = new Array<SkillButton>();
	private int activeIndex;
	private final SpriteActor skillbookIcon = new SpriteActor();
	private final Label levelUpRemainder;
	
	public SkillBar(final GameUi gameUi) {
		setTransform(false);
		this.gameUi = gameUi;
		
		levelUpRemainder = new Label("Click to Level Up!", new LabelStyle(gameUi.getApp().assets.fontSmall, new Color(1f, 1f, 1f, 1f)));
		levelUpRemainder.pack();
		levelUpRemainder.addAction(
				Steps.action(
						Steps.repeat(
							Steps.sequence(
									ActorSteps.alphaTo(0f, 1f, Interpolation.linear),
									ActorSteps.alphaTo(1f, 0.3f, Interpolation.circleOut)))));
		skillbookIcon.setRegion(gameUi.getApp().assets.uiMissing);
		
		holder.setRegion(gameUi.getApp().assets.uiBlock);
		holder.setColor(Color.BLACK);
		
		holder.setHeight(gameUi.getApp().space.vertical(4));
		
		addActor(holder);
		addActor(skillbookIcon);
		
		for(int i = 0; i < 6; i += 1) {
			addButton(new SkillButton(this, gameUi, i + 1));
		}
		setHeight(5f + buttons.get(0).getHeight());
		
		skillbookIcon.setSize(32, 32);
		
		buttons.get(0).grow();
		
		final TouchListener skillBookListener = new TouchListener() {
			@Override
			public void touched() {
				levelUpRemainder.remove();
				gameUi.showSkillbook();
			}
		};
		skillbookIcon.addListener(skillBookListener);
		levelUpRemainder.addListener(skillBookListener);
		addListener(new InputListener() {
			public boolean scrolled(InputEvent event,
				                    final float x,
				                    final float y,
				                    final int amount) {

				int newIndex = activeIndex + -amount;
				
				if(newIndex < 0) {
					newIndex = buttons.size + newIndex;
				}
				else {
					newIndex %= buttons.size;
				}
				
				changeActiveIndex(newIndex);
				
				return true;
			}
		});
		
		buttons.get(0).setState(new SkillState(new SkillInfo.Builder().icon(gameUi.getApp().assets.uiMissing).name("").descrption("").skill(new Skill() {
			@Override
			public float activate(final Actor owner, 
								  final GameWorld gameWorld, 
								  final int skillLevel) {
				
				return 0f;
			}
		}).build()));
		
		buttons.get(0).getState().setCooldown(5f);
		buttons.get(0).getState().setCooldownLeft(5f);
	}
	
	public void markForLevelUp() {
		addActor(levelUpRemainder);
		levelUpRemainder.setPosition(skillbookIcon.getRight(), skillbookIcon.getY() + skillbookIcon.getHeight() / 2f - levelUpRemainder.getHeight() * 0.3f);
	}
	
	private void changeActiveIndex(final int newIndex) {
		buttons.get(activeIndex).shrink();
		buttons.get(newIndex).grow();
		activeIndex = newIndex;
		
		if(gameUi.getDelegate() != null) {
			gameUi.getDelegate().onActiveSkillChanged(buttons.get(newIndex));
		}
	}
	
	private void changeActiveButton(final SkillButton button) {
		for(int i = 0; i < buttons.size; i += 1) {
			if(button == buttons.get(i)) {
				changeActiveIndex(i);
				return;
			}
		}
	}
	
	@Override
	public void setStage(final Stage stage) {
		super.setStage(stage);
		if(stage != null) {
			stage.setScrollFocus(this);
		}
	}
	
	public void addButton(final SkillButton button) {
		buttons.add(button);
		addActor(button);
		button.setY(5f);
	}
	
	public Array<SkillButton> getButtons() {
		return buttons;
	}
	
	@Override
	public void act(final float delta) {
		super.act(delta);
		
		final float space = gameUi.getApp().space.horizontal(1f);
		float layoutX = space;
		for(int i = 0; i < buttons.size; i += 1) {
			final SkillButton button = buttons.get(i);
			
			if(!button.isVisible()) {
				continue;
			}
			
			final float buttonWidth = button.getWidth();
			final float realButtonWidth = buttonWidth * button.getScaleX();

			button.setX(layoutX);
			layoutX += realButtonWidth + space;
		}
		
		setWidth(layoutX);
		setX(getParent().getWidth() / 2f - getWidth() / 2f);
		
		if(Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
			changeActiveIndex(0);
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
			changeActiveIndex(1);
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_3)) {
			changeActiveIndex(2);
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_4)) {
			changeActiveIndex(3);	
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_5)) {
			changeActiveIndex(4);
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_6)) {
			changeActiveIndex(5);
		}
	}
	
	@Override
	public void setWidth(final float width) {
		super.setWidth(width);
		holder.setWidth(width);
		skillbookIcon.setX(width + gameUi.getApp().space.horizontal(1f));
	}
	
}
