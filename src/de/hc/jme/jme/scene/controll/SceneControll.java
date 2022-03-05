/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.jme.jme.scene.controll;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import de.hc.jme.scene.AbstractScene;

/**
 *
 * @author hendrik
 */
public class SceneControll implements PhysicsCollisionListener {
    private static SceneControll instanz = new SceneControll();
    private RigidBodyControl target;
    private int[] points = {0, 0};
    private PhysicsCollisionObject rigidBall;
    private AudioNode audioCrash = null;
    private AudioNode audioKick = null;
    private AudioNode audioBounce = null;

    
    private SceneControll() {
    }
    
    public static SceneControll getDefault() {
        return SceneControll.instanz;
    }
    
    public void startGame(AbstractScene parent) {
        this.target = parent.getBall().getTarget();
        this.rigidBall = (PhysicsCollisionObject) this.target;
        parent.getBulletAppState().getPhysicsSpace().addCollisionListener(this);
        this.audioCrash = new AudioNode(parent.getAssetManager(), "Sounds/crash.wav", AudioData.DataType.Buffer);
        this.audioCrash.setPositional(false);
        this.audioCrash.setLooping(false);
        this.audioCrash.setVolume(2);
        parent.getRootNode().attachChild(this.audioCrash);

        this.audioKick = new AudioNode(parent.getAssetManager(), "Sounds/ball.wav", AudioData.DataType.Buffer);
        this.audioKick.setPositional(false);
        this.audioKick.setLooping(false);
        this.audioKick.setVolume(1);
        parent.getRootNode().attachChild(this.audioKick);
        
        this.audioBounce = new AudioNode(parent.getAssetManager(), "Sounds/bounce.wav", AudioData.DataType.Buffer);
        this.audioBounce.setPositional(false);
        this.audioBounce.setLooping(false);
        this.audioBounce.setVolume(1);
        parent.getRootNode().attachChild(this.audioBounce);
    }
    
    public void checkTarget(AbstractScene parent) {
        if (this.target != null) {
            if (this.target.getPhysicsLocation().y < 80) {
                if (this.target.getPhysicsLocation().z > 0) {
                    this.points[0] ++;
                } else {
                    this.points[1] ++;
                }
                if (this.points[0] >= 6) {
                    AbstractScene.getCurrent().getVehicles()[1].setGameOver();
                } else if (this.points[1] >= 6) {
                    AbstractScene.getCurrent().getVehicles()[0].setGameOver();
                } else {
                    AbstractScene.getCurrent().resetPositions();
                }
            }
        } 

    };
    
    public int[] getPoints() {
        return this.points;
    }
  
    @Override
    public void collision(PhysicsCollisionEvent event) {        
        boolean aCar = event.getObjectA() instanceof VehicleControl;
        boolean bCar = event.getObjectB() instanceof VehicleControl;
      
        boolean aBall = event.getObjectA().equals(this.rigidBall);
        boolean bBall = event.getObjectB().equals(this.rigidBall);
       
        boolean crash = aCar && bCar;
        boolean kick = (aCar && bBall) || (aBall && bCar);
        
        if (crash) {
            this.audioCrash.play();
        }
        if (kick) {
            this.audioKick.play();
        }
        if (!kick && (aBall || bBall)) {
            this.audioBounce.play();
        }

    }

}
