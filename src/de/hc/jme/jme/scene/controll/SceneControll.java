/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.jme.jme.scene.controll;

import com.jme3.bullet.control.RigidBodyControl;
import de.hc.jme.scene.AbstractScene;

/**
 *
 * @author hendrik
 */
public class SceneControll {
    private static SceneControll instanz = new SceneControll();
    private RigidBodyControl target;
    private int[] points = {0, 0};
    
    private SceneControll() {
    }
    
    public static SceneControll getDefault() {
        return SceneControll.instanz;
    }
    
    public void startGame(AbstractScene parent) {
        this.target = parent.getBall().getTarget();
    }
    
    public void checkTarget(AbstractScene parent) {
        if (this.target != null) {
            if (this.target.getPhysicsLocation().y < 80) {
                if (this.target.getPhysicsLocation().z > 0) {
                    this.points[0] ++;
                } else {
                    this.points[1] ++;
                }
                AbstractScene.getCurrent().resetPositions();
            }
//            if(TargetData.getCurrentTresHold() > this.target.getPhysicsLocation().y) {
//                if (this.barrelWait ==  Long.MIN_VALUE) {
//                    this.barrelWait = System.currentTimeMillis();
//                } else if (System.currentTimeMillis() - 5000 > this.barrelWait) {
//                    this.target = null;
//                    this.barrelWait = Long.MIN_VALUE;
//                    this.target = TargetData.initNext(parent);
//                }
//            }
        } 

    };
    
    public int[] getPoints() {
        return this.points;
    }

}
