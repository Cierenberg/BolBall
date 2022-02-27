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
public class Ground{
    private F40Scene parent;
    private Spatial groundSpatial;
    public static final float HEIGHT = 1f;
    private RigidBodyControl groundPhysikAlias;
    
    public Ground(F40Scene parent, Vector3f position) {
        this.parent = parent;
        this.init(position);
    }

    public Spatial getSpatial() {
        return this.groundSpatial;
    }
              
    private void init(Vector3f position) {
        Material groundMaterial = new Material(this.parent.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
        groundMaterial.setTexture("ColorMap", this.parent.getAssetManager().loadTexture("Textures/feld.png"));
        
        Float offY = this.parent.getEnvironmentHeight(position.x, position.z);
        if (offY != null) {
            position.y = offY + Ground.HEIGHT / 2f;
        }     
        this.groundSpatial = this.parent.getAssetManager().loadModel("Models/feld_export.j3o");
        
        this.groundSpatial.scale(2f,6f,2f);
        this.groundSpatial.setLocalTranslation(position);
        groundSpatial.setMaterial(groundMaterial);
        this.groundPhysikAlias = new RigidBodyControl(0f);
        groundSpatial.addControl(this.groundPhysikAlias);
        this.parent.getPhysicsSpace().add(this.groundPhysikAlias);
        this.groundSpatial.setShadowMode(RenderQueue.ShadowMode.Receive);

        this.parent.getRootNode().attachChild(this.groundSpatial);
    }
    
    
    private void updatePosition(Vector3f position) {
        this.groundSpatial.setLocalTranslation(position);
    }
}
