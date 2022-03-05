/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.jme.jme.models.vehicle;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import de.hc.jme.scene.AbstractScene;


/**
 *
 * @author hendrik
 */
public class F40 extends AbstractVehicle {
    
    public F40(AbstractScene parent, Vector3f initPosition, float rotateY) {
        super(parent, initPosition, rotateY);
    }

    @Override
    public Material getBodyTextureMaterial() {
        Material mat_body = new Material(
            this.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat_body.setTexture("ColorMap",
            this.getAssetManager().loadTexture("Textures/f40.png"));
        return mat_body;
    }

    @Override
    public Spatial getBodyModel() {
        return this.getAssetManager().loadModel("Models/f40_export3.j3o"); 
    }
    
    public CompoundCollisionShape getCompoundCollisionShape() {
        CompoundCollisionShape compoundShape = new CompoundCollisionShape();
        BoxCollisionShape base = new BoxCollisionShape(new Vector3f(1.5f, 0.3f, 3.3f));
        BoxCollisionShape hut = new BoxCollisionShape(new Vector3f(1.3f, 0.3f, 1.2f));
        BoxCollisionShape heck = new BoxCollisionShape(new Vector3f(1.5f, 0.3f, 0.3f));

        compoundShape.addChildShape(base, new Vector3f(0, 0.5f, -0.4f));
        compoundShape.addChildShape(hut, new Vector3f(0, 1f, -.5f));
        compoundShape.addChildShape(heck, new Vector3f(0, 1f, -3.2f));
        
        return compoundShape;
    }

    @Override
    public Vector3f getLocalBodyTranslation() {
        return new Vector3f(0f, 0.5f, -.05f);
    }

    @Override
    public Vector3f getBodyScale() {
        return new Vector3f(0.9f, 0.9f, 0.9f);
    }

    @Override
    public int getWeight() {
        return 100;
    }

    @Override
    public float getStiffness() {
        return 140f;
    }

    @Override
    public float getCompValue() {
        return 0.3f;
    }

    @Override
    public float getDampValue() {
        return 0.6f;
    }

    @Override
    public Material getWheelMaterial() {
        Material mat_wheel = new Material(
                this.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat_wheel.setTexture("ColorMap",
                this.getAssetManager().loadTexture("Textures/rad_alpha.png"));
        return mat_wheel;
    }

    @Override
    public Spatial getWhellModel() {
        return this.getAssetManager().loadModel("Models/wheel_export.j3o");
    }

    @Override
    public Vector3f getWheelScale() {
        return new Vector3f(0.7f, 0.7f, 0.7f);
    }

    
}
