/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.jme.gui.hud;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import de.hc.jme.jme.scene.controll.SceneControll;
import de.hc.jme.scene.F40Scene;

/**
 *
 * @author hendrik
 */
public class Hud {
    private final static Hud instance = new Hud();
    private Node guiNode;
    private F40Scene parent;
    private BitmapFont arialFont;
    private BitmapText guiText[] = new BitmapText[5];
    private float[][] padPosition = {{0, 0, 0, 0}, {0, 0, 0, 0}};
    private long targetTime = Long.MAX_VALUE;
    private AudioNode audioGameOver = null;
    private AudioNode audioCongratulation = null;
    private boolean hide = true; 
    private float[] displayDimension; 
    private final Picture[] picPad = {new Picture("Pad Picture"), new Picture("Pad Picture")};
    private final Picture picHappy = new Picture("congratulation");
    private final Picture picForward = new Picture("Gear Picture forward");
    private final Picture picBackward = new Picture("Gear Picture backward");
    private Picture pic = new Picture("Gear Picture");
    private final Picture picSad = new Picture("Game over");
    
    private final Picture picTacho = new Picture("Tacho back");
    private Object[] updateable = new Object[50];
    private long lastUpdate = System.currentTimeMillis();
    
    
    private Hud() {
    }
    
    public static Hud getDefault() {
        return Hud.instance;
    }
    
    public void setHide(boolean hide) {
        this.hide = hide;
    }
    
    public void setParent(F40Scene parent) {
        if (this.parent == null) {
            
            this.parent = parent;
            this.displayDimension = new float[]{this.parent.getAppSettings().getWidth(), this.parent.getAppSettings().getHeight()}; 
            this.guiNode = this.parent.getGuiNode();
            this.arialFont = this.parent.getAssetManager().loadFont("Interface/Fonts/digital.fnt");
            this.audioGameOver = new AudioNode(this.parent.getAssetManager(), "Sounds/sadtrombone.wav", AudioData.DataType.Buffer);
            this.audioGameOver.setPositional(false);
            this.audioGameOver.setLooping(false);
            this.audioGameOver.setVolume(2);
            this.parent.getRootNode().attachChild(this.audioGameOver);

            this.audioCongratulation = new AudioNode(this.parent.getAssetManager(), "Sounds/Beifahl.wav", AudioData.DataType.Buffer);
            this.audioCongratulation.setPositional(false);
            this.audioCongratulation.setLooping(false);
            this.audioCongratulation.setVolume(2);
            this.parent.getRootNode().attachChild(this.audioCongratulation);
            
            this.picPad[0].setImage(this.parent.getAssetManager(), "Textures/keypad.png", true);
            this.picPad[1].setImage(this.parent.getAssetManager(), "Textures/keypad.png", true);
            this.picHappy.setImage(this.parent.getAssetManager(), "Textures/congratulation.png", true);
            this.picForward.setImage(this.parent.getAssetManager(), "Textures/forward.png", true);
            this.picBackward.setImage(this.parent.getAssetManager(), "Textures/backward.png", true);
            this.picTacho.setImage(this.parent.getAssetManager(), "Textures/tacho.png", true);
            this.picSad.setImage(this.parent.getAssetManager(), "Textures/gameover.png", true);
            
            this.parent.getRootNode().attachChild(this.audioGameOver);
            this.parent.getRootNode().attachChild(this.audioCongratulation);
        }
    }
    
    public void playGameOverSound() {
        this.audioGameOver.play();
    }
    
    public void playCongratulationSound() {
        this.audioCongratulation.play();
    }
    
    public void startTargetTime() {
        this.targetTime = System.currentTimeMillis() + 1000 * 60 * 7;
//        this.targetTime = System.currentTimeMillis() + 1000 * 10;
    }
    
    public boolean isTagetTimeStarted() {
        return this.targetTime != Long.MAX_VALUE;
    }
    
    public void setText(String text, ColorRGBA color) {
        if (this.guiText[0] != null) {
            this.guiText[0].setColor(color);
            this.guiText[0].setText(text);
        }
    }
    
    public void clean() {
        if (this.guiNode != null) {
            this.guiNode.detachAllChildren();
        }
    }
    
    public void update() {
        if (this.guiNode != null) {
            if (!this.hide) {
                boolean updateHud = false;
                if (this.parent.getAppSettings().getWidth() != this.displayDimension[0]); {
                    this.displayDimension = new float[]{this.parent.getAppSettings().getWidth(), this.parent.getAppSettings().getHeight()};
                    updateHud = true;
                }
                if (this.parent != null && this.parent.getVehicles() != null && this.parent.getVehicles().length > 0) {
                    if (this.updateable[0] == null) {
                        this.updateable[0] = this.parent.getVehicles()[0].isCongratulation();
                        this.updateable[1] = this.parent.getVehicles()[0].isGameOver();
                        this.updateable[2] = this.parent.getVehicles()[1].isCongratulation();
                        this.updateable[3] = this.parent.getVehicles()[1].isGameOver();
                        this.updateable[4] = SceneControll.getDefault().getPoints()[0];
                        this.updateable[5] = SceneControll.getDefault().getPoints()[1];
                    }
                    
                    if (this.parent.getVehicles()[0].isCongratulation() != (boolean) this.updateable[0]) {
                        this.updateable[0] = this.parent.getVehicles()[0].isCongratulation();
                        updateHud = true;
                    }
                    if (this.parent.getVehicles()[0].isGameOver() != (boolean) this.updateable[1]) {
                        this.updateable[1] = this.parent.getVehicles()[0].isGameOver();
                        updateHud = true;
                    }
                    if (this.parent.getVehicles()[1].isCongratulation() != (boolean) this.updateable[2]) {
                        this.updateable[2] = this.parent.getVehicles()[1].isCongratulation();
                        updateHud = true;
                    }
                    if (this.parent.getVehicles()[1].isGameOver() != (boolean) this.updateable[3]) {
                        this.updateable[3] = this.parent.getVehicles()[1].isGameOver();
                        updateHud = true;
                    }
                    if (SceneControll.getDefault().getPoints()[0] != (int) this.updateable[4]) {
                        this.updateable[4] = SceneControll.getDefault().getPoints()[0];
                        updateHud = true;
                    }
                    if (SceneControll.getDefault().getPoints()[1] != (int) this.updateable[5]) {
                        this.updateable[5] = SceneControll.getDefault().getPoints()[1];
                        updateHud = true;
                    }

                    
                    if (!updateHud) {
                        if (System.currentTimeMillis() - this.lastUpdate > 1000) {
                            updateHud = true;
                        }
                    }
                }

                if (updateHud) {
                    this.lastUpdate = System.currentTimeMillis();
                    this.guiNode.detachAllChildren();
                    if (!this.parent.shouldBeonDesktop() && !this.parent.getVehicles()[0].isGameOver() && !this.parent.getVehicles()[1].isGameOver()) {        
                        this.padPosition[0] = new float[] {this.displayDimension[0] - this.displayDimension[0]/3.5f, this.displayDimension[1]/14, this.displayDimension[0]/4, displayDimension[0]/4}; 
                        this.padPosition[1] = new float[] {this.displayDimension[0]/3.5f - this.displayDimension[0]/4, this.displayDimension[1] - (this.displayDimension[1]/14 + displayDimension[0]/4), this.displayDimension[0]/4, displayDimension[0]/4}; 
                        this.picPad[0].setWidth(this.padPosition[0][2]);
                        this.picPad[0].setHeight(this.padPosition[0][3]);
                        this.picPad[0].setPosition(this.padPosition[0][0], this.padPosition[0][1]);
                        this.picPad[1].setWidth(this.padPosition[1][2]);
                        this.picPad[1].setHeight(this.padPosition[1][3]);
                        this.picPad[1].setPosition(this.padPosition[1][0], this.padPosition[1][1]);
                        this.guiNode.attachChild(this.picPad[0]);
                        this.guiNode.attachChild(this.picPad[1]);
                    }
               
                    
                    if (this.parent.getVehicles()[0] != null) {
                        String score = "RED " + SceneControll.getDefault().getPoints()[0] + " / ";
                        score += "BLUE " + SceneControll.getDefault().getPoints()[0] + " / ";
                        BitmapText textDisplayScoreRed = new BitmapText(this.arialFont, false);
                        textDisplayScoreRed.setSize(this.arialFont.getCharSet().getRenderedSize() * 2); 
                        textDisplayScoreRed.setColor(ColorRGBA.Red);
                        textDisplayScoreRed.setText("" + SceneControll.getDefault().getPoints()[0]);
                        textDisplayScoreRed.setLocalTranslation(10f, 50f, 10f);
                        this.guiNode.attachChild(textDisplayScoreRed);
                        BitmapText textDisplayScoreBlue = new BitmapText(this.arialFont, false);
                        textDisplayScoreBlue.setSize(this.arialFont.getCharSet().getRenderedSize() * 2); 
                        textDisplayScoreBlue.setColor(ColorRGBA.Blue);
                        textDisplayScoreBlue.setText("" + SceneControll.getDefault().getPoints()[1]);
                        textDisplayScoreBlue.setLocalTranslation(displayDimension[0] - 30f, displayDimension[1] - 1, 10f);
                        this.guiNode.attachChild(textDisplayScoreBlue);
                        
                        if (this.parent.getVehicles()[0].isCongratulation()) {                             
                            float picWidth = this.displayDimension[1];
                            this.picHappy.setWidth(picWidth);
                            this.picHappy.setHeight(picWidth / 2);
                            this.picHappy.setPosition((this.displayDimension[0] - picWidth) / 2, (this.displayDimension[1] / 2 - (picWidth / 2)) / 2);
                            this.guiNode.attachChild(picHappy);     
                        } else if(this.parent.getVehicles()[0].isGameOver()) {                                                        
                            float picWidth = this.displayDimension[1];
                            this.picSad.setWidth(picWidth);
                            this.picSad.setHeight(picWidth / 2);
                            this.picSad.setPosition((this.displayDimension[0] - picWidth) / 2, (this.displayDimension[1] / 2 - (picWidth / 2)) / 2);
                            this.guiNode.attachChild(picSad);     
                        }
                    }
                    if (this.parent.getVehicles()[1] != null) {
                        if (this.parent.getVehicles()[1].isCongratulation()) {                             
                            float picWidth = this.displayDimension[1];
                            this.picHappy.setWidth(picWidth);
                            this.picHappy.setHeight(picWidth / 2);
                            this.picHappy.setPosition((this.displayDimension[0] - picWidth) / 2, (this.displayDimension[1] / 2 + (picWidth / 2)) / 2);
                            this.guiNode.attachChild(picHappy);     
                        } else if(this.parent.getVehicles()[1].isGameOver()) {                                                        
                            float picWidth = this.displayDimension[1];
                            this.picSad.setWidth(picWidth);
                            this.picSad.setHeight(picWidth / 2);
                            this.picSad.setPosition((this.displayDimension[0] - picWidth) / 2, (this.displayDimension[1] / 2 + (picWidth / 2)) / 2);
                            this.guiNode.attachChild(picSad);     
                        }
                    }
                }
            }
        }
    }
    
        /**
     * Touch.
     */
    public void touch() {
        if (this.parent != null) {
            if (!this.parent.shouldBeonDesktop()) {
                float touchWidthX = this.padPosition[0][2] / 3;
                float touchWidthY = touchWidthX;
                Vector2f position = this.parent.getInputManager().getCursorPosition();

                if (position.getX() > this.padPosition[0][0] && position.getX() < (this.padPosition[0][0] + this.padPosition[0][2]) && position.getY() > this.padPosition[0][1] && position.getY() < (this.padPosition[0][1] + this.padPosition[0][3])) {
                    float iX = position.getX() - this.padPosition[0][0];
                    float iY = position.getY() - this.padPosition[0][1];
                    boolean steering = false;
                    if (iX < touchWidthX) {
                            this.parent.getVehicles()[0].steerLeft();
                            steering = true;
                    }
                    if (iX > this.padPosition[0][2] - touchWidthX) {
                            this.parent.getVehicles()[0].steerRight();
                            steering = true;
                    }
                    if (iY < touchWidthY) {
                            this.parent.getVehicles()[0].brake();
                            steering = true;
                    }
                    if (iY > this.padPosition[0][3] - touchWidthY) {
                            this.parent.getVehicles()[0].accelerate();
                            steering = true;
                    }
                    if (!steering) {
                        this.parent.getVehicles()[0].horn();
                    }
                }
                
                
                if (position.getX() > this.padPosition[1][0] && position.getX() < (this.padPosition[1][0] + this.padPosition[1][2]) && position.getY() > this.padPosition[1][1] && position.getY() < (this.padPosition[1][1] + this.padPosition[1][3])) {
                    float iX = position.getX() - this.padPosition[1][0];
                    float iY = position.getY() - this.padPosition[1][1];
                    boolean steering = false;
                    if (iX < touchWidthX) {
                            this.parent.getVehicles()[1].steerLeft();
                            steering = true;
                    }
                    if (iX > this.padPosition[1][2] - touchWidthX) {
                            this.parent.getVehicles()[1].steerRight();
                            steering = true;
                    }
                    if (iY < touchWidthY) {
                            this.parent.getVehicles()[1].brake();
                            steering = true;
                    }
                    if (iY > this.padPosition[1][3] - touchWidthY) {
                            this.parent.getVehicles()[1].accelerate();
                            steering = true;
                    }
                    if (!steering) {
                        this.parent.getVehicles()[1].horn();
                    }
                }
            }
        }
    }

}
