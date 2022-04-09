/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.hc.jme.models;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import de.hc.jme.scene.F40Scene;

/**
 *
 * @author hendrik
 */
public class Loop{
    private F40Scene parent;
    private Spatial loopSpatial;
    public static final float HEIGHT = 25f;
    private RigidBodyControl loopPhysikAlias;
    
    public Loop(F40Scene parent, Vector3f position) {
        this.parent = parent;
        this.init(position);
    }

    public Spatial getSpatial() {
        return this.loopSpatial;
    }
              
    private void init(Vector3f position) {
        Material loopMaterial = new Material(this.parent.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
        loopMaterial.setTexture("ColorMap", this.parent.getAssetManager().loadTexture("Textures/stadion2.png"));
        
        Float offY = this.parent.getEnvironmentHeight(position.x, position.z);
       
        this.loopSpatial = this.parent.getAssetManager().loadModel("Models/LOOP3.j3o");
        
//        this.loopSpatial.scale(5f);
        
        
        this.loopSpatial.scale(5f, 5f, 10f);
        
        
        this.loopSpatial.rotate(0f, (float) Math.toRadians(-90), 0f);
        this.loopSpatial.setLocalTranslation(position);
        loopSpatial.setMaterial(loopMaterial);
        this.loopPhysikAlias = new RigidBodyControl(0f);
        loopSpatial.addControl(this.loopPhysikAlias);
        this.parent.getPhysicsSpace().add(this.loopPhysikAlias);
        this.loopSpatial.setShadowMode(RenderQueue.ShadowMode.Cast);

        this.parent.getRootNode().attachChild(this.loopSpatial);
    }
    
    
    private void updatePosition(Vector3f position) {
        this.loopSpatial.setLocalTranslation(position);
    }
}
