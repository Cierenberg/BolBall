package de.hc.jme.jme.models.vehicle;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import de.hc.jme.scene.AbstractScene;

/**
 *
 * @author hendrik
 */
public class F40Blue extends F40{
    
    public F40Blue(AbstractScene parent, boolean sport, Vector3f initPosition, float rotateY) {
        super(parent, sport, initPosition, rotateY);
    }
    
    @Override
    public Material getBodyTextureMaterial() {
        Material mat_body = new Material(
            this.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat_body.setTexture("ColorMap",
            this.getAssetManager().loadTexture("Textures/f40_blau.png"));
        return mat_body;
    }
}