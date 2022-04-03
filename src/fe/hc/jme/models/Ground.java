/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.hc.jme.models;


import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import de.hc.jme.jme.utility.Utility;
import de.hc.jme.scene.F40Scene;

/**
 *
 * @author hendrik
 */
public class Ground{
    private F40Scene parent;
    private Spatial[] groundSpatial = {null, null};
    public static final float HEIGHT = 1f;
    private RigidBodyControl[] groundPhysikAlias = {null, null};
    
    public Ground(F40Scene parent, Vector3f position) {
        this.parent = parent;
        this.init(position);
    }

    public Spatial[] getSpatial() {
        return this.groundSpatial;
    }
              
    private void init(Vector3f position) {
        Material groundMaterial = new Material(this.parent.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
        groundMaterial.setTexture("ColorMap", this.parent.getAssetManager().loadTexture("Textures/beton_green.png"));
        
        System.out.println(this.parent);
        System.out.println(this.parent.getAssetManager());
        
        this.groundSpatial[0] = this.parent.getAssetManager().loadModel("Models/stadion2_ground.j3o");
        
        this.groundSpatial[0].scale(10f,6f,20f);
        this.groundSpatial[0].setLocalTranslation(position);
        
        
        Utility.setTextureScale(this.groundSpatial[0], new Vector2f(3, 6));        
        Texture floorTexture = this.parent.getAssetManager().loadTexture("Textures/beton_green.png");
        floorTexture.setWrap(Texture.WrapMode.Repeat);
        groundMaterial.setTexture("ColorMap",floorTexture);
        
        
        this.groundSpatial[0].setMaterial(groundMaterial);
        this.groundPhysikAlias[0] = new RigidBodyControl(0f);
        this.groundSpatial[0].addControl(this.groundPhysikAlias[0]);
        this.parent.getPhysicsSpace().add(this.groundPhysikAlias[0]);
        this.groundSpatial[0].setShadowMode(RenderQueue.ShadowMode.Receive);

        this.parent.getRootNode().attachChild(this.groundSpatial[0]);
        
        Material stadionMaterial = new Material(this.parent.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
        stadionMaterial.setTexture("ColorMap", this.parent.getAssetManager().loadTexture("Textures/stadion2.png"));
        
        
//        Material stadionMaterial = new Material(this.parent.getAssetManager(),
//        "Common/MatDefs/Light/Lighting.j3md");
//        stadionMaterial.setTexture("DiffuseMap",
//        this.parent.getAssetManager().loadTexture("Textures/stadion2.png"));
//        stadionMaterial.setBoolean("UseMaterialColors",true);
//        stadionMaterial.setBoolean("VertexLighting", true);
//        stadionMaterial.setColor("Diffuse",ColorRGBA.White);
//        stadionMaterial.setColor("Specular",ColorRGBA.White);       
//        stadionMaterial.setFloat("Shininess", 120f);
//        
        
        this.groundSpatial[1] = this.parent.getAssetManager().loadModel("Models/stadion2.j3o");
        
        this.groundSpatial[1].scale(10f,6f,20f);
        this.groundSpatial[1].setLocalTranslation(position);
        this.groundSpatial[1].rotate(0f, (float) Math.toRadians(180), 0f);
        
        this.groundSpatial[1].setMaterial(stadionMaterial);
        this.groundPhysikAlias[1] = new RigidBodyControl(0f);
        this.groundSpatial[1].addControl(this.groundPhysikAlias[1]);
        this.parent.getPhysicsSpace().add(this.groundPhysikAlias[1]);
        this.groundSpatial[1].setShadowMode(RenderQueue.ShadowMode.Receive);

        this.parent.getRootNode().attachChild(this.groundSpatial[1]);



    }
    
    
    private void updatePosition(Vector3f position) {
        this.groundSpatial[0].setLocalTranslation(position);        
        this.groundSpatial[1].setLocalTranslation(position);
    }
}
