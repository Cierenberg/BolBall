package fe.hc.jme.models;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.hc.jme.jme.utility.ITarget;
import de.hc.jme.scene.AbstractScene;

import java.util.ArrayList;
import java.util.List;
import de.hc.jme.scene.F40Scene;

/**
 *
 * @author hendrik
 */
public class Ball implements ITarget{
    public static enum TYPE{
        SOCCER,RUGBY,PUC;
    }

    public static enum SIZE{
        SMALL,BIG;
    }
    
    private static int count = 0;
    private static final List<RigidBodyControl> rigidBodyControols = new ArrayList<>();
    private Node ball = new Node("Ball_" + (++ Ball.count));
    private Material material[];
    private AbstractScene parent;
    private Vector3f position;
    private RigidBodyControl ballPhysikAlias;
    
    public Ball(AbstractScene parent, Vector3f position, boolean calculateY, Ball.TYPE type, Ball.SIZE size) {
        this.parent = parent;
        this.position = position;
        this.material = new Material[] {new Material(this.parent.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md"), new Material(this.parent.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md"), new Material(this.parent.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md")};
        this.material[0].setTexture("ColorMap", this.parent.getAssetManager().loadTexture("Textures/bw.png"));
        this.material[1].setTexture("ColorMap", this.parent.getAssetManager().loadTexture("Textures/amfbt.png"));
        this.material[2].setTexture("ColorMap", this.parent.getAssetManager().loadTexture("Textures/puc.png"));
        if (calculateY) {
        Float offY = this.parent.getEnvironmentHeight(this.position.x, this.position.z);
            if (offY != null) {
                this.position.y = offY + Barrel.HEIGHT / 2f;
            }
        }
        this.init(type, size);
    }
    
    private void init(Ball.TYPE type, Ball.SIZE size) {
        Spatial ballSpatial = null;
        if (type == Ball.TYPE.SOCCER) {
            ballSpatial = this.parent.getAssetManager().loadModel("Models/ball_export.j3o");
            ballSpatial.setMaterial(this.material[0]);
            ballSpatial.setLocalTranslation(this.position);
            if (size == Ball.SIZE.SMALL) {
                ballSpatial.scale(0.5f, 0.5f, 0.5f);
            } else {
                ballSpatial.scale(1.2f, 1.2f, 1.2f);
            }
            this.ballPhysikAlias = new RigidBodyControl(30f);
            ballSpatial.addControl(this.ballPhysikAlias);
            this.parent.getPhysicsSpace().add(ballPhysikAlias);
            Ball.rigidBodyControols.add(ballPhysikAlias);
            ballSpatial.setShadowMode(RenderQueue.ShadowMode.Cast);
        } else if (type == Ball.TYPE.RUGBY) {
            ballSpatial = this.parent.getAssetManager().loadModel("Models/amfb_export.j3o");
            ballSpatial.setMaterial(this.material[1]);
            ballSpatial.setLocalTranslation(this.position);
            if (size == Ball.SIZE.SMALL) {
                ballSpatial.scale(0.5f, 0.5f, 0.5f);
            } else {
                ballSpatial.scale(1.2f, 1.2f, 1.2f);
            }
            this.ballPhysikAlias = new RigidBodyControl(30f);
            ballSpatial.addControl(this.ballPhysikAlias);
            this.parent.getPhysicsSpace().add(ballPhysikAlias);
            Ball.rigidBodyControols.add(ballPhysikAlias);
            ballSpatial.setShadowMode(RenderQueue.ShadowMode.Cast);
        } else if (type == Ball.TYPE.PUC) {
            ballSpatial = this.parent.getAssetManager().loadModel("Models/puc_export.j3o");
            ballSpatial.setMaterial(this.material[2]);
            ballSpatial.setLocalTranslation(this.position);
            if (size == Ball.SIZE.SMALL) {
                ballSpatial.scale(0.5f, 0.5f, 0.5f);
            } else {
                ballSpatial.scale(1.2f, 1.2f, 1.2f);
            }
            this.ballPhysikAlias = new RigidBodyControl(30f);
            ballSpatial.addControl(this.ballPhysikAlias);
            this.parent.getPhysicsSpace().add(ballPhysikAlias);
            Ball.rigidBodyControols.add(ballPhysikAlias);
            ballSpatial.setShadowMode(RenderQueue.ShadowMode.Cast);
        } else {
            throw new RuntimeException("not jet supported");
        }

        this.ball.attachChild(ballSpatial);
        this.ballPhysikAlias.setCollisionGroup(3);
    }
    
    public void Replace() {
        this.ballPhysikAlias.setPhysicsLocation(this.position);
        this.ballPhysikAlias.setLinearVelocity(Vector3f.ZERO);
        this.ballPhysikAlias.setAngularVelocity(Vector3f.ZERO);
    }
    
    public Node getNode(F40Scene parent) {
        return this.ball;
    }

    @Override
    public RigidBodyControl getTarget() {
        return this.ballPhysikAlias;
    }
    
    public static void removeAllControls(AbstractScene parent) {
        for (RigidBodyControl control : Ball.rigidBodyControols) {
            parent.getPhysicsSpace().remove(control);
        }
        Ball.rigidBodyControols.clear();
    }

    @Override
    public Node getNode() {
        return this.ball;
    }

    @Override
    public float getTreshold() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
