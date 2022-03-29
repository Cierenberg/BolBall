/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.jme.jme.models.vehicle;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
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
public class Lambo extends AbstractVehicle {
    
    public Lambo(AbstractScene parent, Vector3f initPosition, float rotateY) {
        super(parent, initPosition, rotateY);
        this.vehicleNode.detachChild(this.audioHorn);
        this.audioHorn = new AudioNode(parent.getAssetManager(), "Sounds/horn2.wav", AudioData.DataType.Buffer);
        this.audioHorn.setPositional(false);
        this.audioHorn.setLooping(false);
        this.audioHorn.setVolume(1);
        this.vehicleNode.attachChild(this.audioHorn);

    }

    @Override
    public Material getBodyTextureMaterial() {
        Material mat_body = new Material(
            this.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat_body.setTexture("ColorMap",
            this.getAssetManager().loadTexture("Textures/all_frei.png"));
        return mat_body;
    }

    @Override
    public Spatial getBodyModel() {
        return this.getAssetManager().loadModel("Models/lambo_export.j3o"); 
    }
    
    public CompoundCollisionShape getCompoundCollisionShape() {
        CompoundCollisionShape compoundShape = new CompoundCollisionShape();
        BoxCollisionShape base = new BoxCollisionShape(new Vector3f(1.5f, 0.3f, 3.3f));
        BoxCollisionShape hut = new BoxCollisionShape(new Vector3f(1.3f, 0.3f, 1.2f));
        BoxCollisionShape heck = new BoxCollisionShape(new Vector3f(1.5f, 0.3f, 0.3f));
        BoxCollisionShape flkf = new BoxCollisionShape(new Vector3f(.2f, 0.1f, 0.4f));     
        BoxCollisionShape frkf = new BoxCollisionShape(new Vector3f(.2f, 0.1f, 0.4f));     
        
        
        compoundShape.addChildShape(base, new Vector3f(0, 0.5f, 0f));
        compoundShape.addChildShape(hut, new Vector3f(0, 1f, -.1f));
        compoundShape.addChildShape(heck, new Vector3f(0, 1f, -3f));
        compoundShape.addChildShape(flkf, new Vector3f(1.4f, 0.3f, +3.7f));
        compoundShape.addChildShape(frkf, new Vector3f(-1.4f, 0.3f, +3.7f));
        
        return compoundShape;
    }

    @Override
    public Vector3f getLocalBodyTranslation() {
        return new Vector3f(0f, 0.6f, -.55f);
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
                this.getAssetManager().loadTexture("Textures/felge_alpha_l.png"));
        return mat_wheel;
    }

    @Override
    public Spatial getWhellModel(boolean rear) {
        Spatial wheel = this.getAssetManager().loadModel("Models/wheel_export2.j3o");
        if (rear) {
            wheel.scale(1.2f, 1.3f, 1.2f);
        }
        return this.getAssetManager().loadModel("Models/wheel_export2.j3o");
    }

    @Override
    public Vector3f getWheelScale() {
        return new Vector3f(0.8f, 1.1f, 0.8f);
    }

    @Override
    public float getBodyYaw() {
        return (float) Math.toRadians(1);
    }

    
}
