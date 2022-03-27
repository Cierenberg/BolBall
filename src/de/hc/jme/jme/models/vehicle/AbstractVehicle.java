/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.jme.jme.models.vehicle;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import de.hc.jme.gui.hud.Hud;
import de.hc.jme.jme.scene.controll.SceneControll;
import de.hc.jme.scene.AbstractScene;
import fe.hc.jme.models.Arrow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hendrik
 */
public abstract class AbstractVehicle {

    private AbstractScene parent;
    private AssetManager assetManager;
    protected VehicleControl vehicle;
    private float maxAccelerationForce = 1.5f;
    private float maxSpeed = 100;
    private float brakeForce = 20.0f;
    private float[] maxSteeringValue = {0.5f, 0.2f};
    private float currentSteeringValue = 0.0f;
    private float[] steeringDeltaValue = {0.1f, 0.01f};
    private float accelerationValue = 0;
    private float maxSuspensionForce = 10000.0f;
    final private Vector3f jumpForce = new Vector3f(0, 2000, 0);
    private Node vehicleNode;
    private boolean sport;
    private boolean forward = true;
    protected Camera cam;
    protected Vector3f camTarget;
    private List<Vector3f> lastPositions = new ArrayList<>();
    boolean initCampos = false;
    private float rotateY;
    private Quaternion rotation;
    private Vector3f initPosition;
    private RigidBodyControl target;
    private Arrow arrow;
    private long lastPositionChange = Long.MAX_VALUE;
    public boolean gameOver = false;
    public boolean congratulation = false;
    public long gameOverSince = Long.MAX_VALUE;
    private MovingMetrics metrics;
    private Spatial body = null;
    private float targetYaw = 0f;
    private float curentYaw = 0f;
    private float yawRotation = 0;
    private float bodyRotation = 0.02f;
    private float targetRoll = 0f;
    private float curentRoll = 0f;
    private final float bodyRotationFaktor = .6f;
    private float rollRotation = 0;
    private int steering = 0;
    boolean key[][]
            = {
                {false, false, false},
                {false, false, false},
                {false, false, false}
            };
    private long lastPressed = System.currentTimeMillis();
    private final Map<Node, Spatial> wheelMap = new HashMap<Node, Spatial>();
    private final Map<Long, Node> particleEmitterMap = new HashMap<>();
    private final Map<Node, ParticleEmitter> particleEmitterMap2 = new HashMap<>();
    private final Material smokeMat;
    private AudioNode audioGo = null;
    private AudioNode audioRun = null;
    private AudioNode audioVelo = null;
    private AudioNode audioHorn = null;
    private AudioNode audioBack = null;
    private AudioNode audioYeehaw = null;
    private AudioNode audioGoal = null;
    private long lastYehaw = System.currentTimeMillis();
    private float[] runPitch = {0.5f, 0.5f, 1.5f, 0.004f};

    public AbstractVehicle(AbstractScene parent, Vector3f initPosition, float rotateY) {
        this.parent = parent;
        this.sport = sport;
        this.rotateY = rotateY;
        Float offY = this.parent.getEnvironmentHeight(initPosition.x, initPosition.z);
        if (offY != null) {
            initPosition.y = offY + 2f;
        }
        this.vehicleNode = new Node("vehicleNode");
        this.assetManager = this.parent.getAssetManager();
        this.smokeMat = new Material(this.parent.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        this.smokeMat.setTexture("Texture", this.parent.getAssetManager().loadTexture("Textures/dust.png"));
        this.smokeMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        this.smokeMat.setBoolean("PointSprite", true);

        this.audioGo = new AudioNode(assetManager, "Sounds/zuendung.wav", AudioData.DataType.Buffer);
        this.audioGo.setPositional(false);
        this.audioGo.setLooping(false);
        this.audioGo.setVolume(2);
        this.vehicleNode.attachChild(this.audioGo);

        this.audioRun = new AudioNode(assetManager, "Sounds/motor.wav", AudioData.DataType.Buffer);
        this.audioRun.setPositional(false);
        this.audioRun.setLooping(true);
        this.audioRun.setVolume(1);
        this.audioRun.setPitch(this.runPitch[1]);
        this.vehicleNode.attachChild(this.audioRun);

        this.audioVelo = new AudioNode(assetManager, "Sounds/test2.wav", AudioData.DataType.Buffer);
        this.audioVelo.setPositional(false);
        this.audioVelo.setLooping(false);
        this.audioVelo.setVolume(1);
        this.vehicleNode.attachChild(this.audioVelo);

        this.audioHorn = new AudioNode(assetManager, "Sounds/horn.wav", AudioData.DataType.Buffer);
        this.audioHorn.setPositional(false);
        this.audioHorn.setLooping(false);
        this.audioHorn.setVolume(1);
        this.vehicleNode.attachChild(this.audioHorn);

        this.audioBack = new AudioNode(assetManager, "Sounds/piepen.wav", AudioData.DataType.Buffer);
        this.audioBack.setPositional(false);
        this.audioBack.setLooping(true);
        this.audioBack.setVolume(1);
        this.vehicleNode.attachChild(this.audioBack);

        this.audioYeehaw = new AudioNode(assetManager, "Sounds/yeehaw2.wav", AudioData.DataType.Buffer);
        this.audioYeehaw.setPositional(false);
        this.audioYeehaw.setLooping(false);
        this.audioYeehaw.setVolume(1);
        this.vehicleNode.attachChild(this.audioYeehaw);

        this.audioGoal = new AudioNode(assetManager, "Sounds/goal.wav", AudioData.DataType.Buffer);
        this.audioGoal.setPositional(false);
        this.audioGoal.setLooping(false);
        this.audioGoal.setVolume(1);
        this.vehicleNode.attachChild(this.audioGoal);
        
        this.audioGo.play();

        this.initJeep(initPosition);
        this.audioRun.play();
    }

    public AssetManager getAssetManager() {
        return this.parent.getAssetManager();
    }

    public void horn() {
        this.audioHorn.play();
    }

    public void setTarget(RigidBodyControl target) {
        this.target = target;
    }

    public void setCam(Camera cam) {
        this.cam = cam;
        this.lastPositions.clear();
        this.lastPositions.add(this.vehicle.getPhysicsLocation());
        this.camTarget = this.cam.getLocation();
        this.updateCam();
        this.cam.setLocation(camTarget);
    }

    public boolean isCongratulation() {
        return this.congratulation;
    }

    public void setCongratulation() {
        this.congratulation = true;
        this.gameOver = true;
        this.gameOverSince = System.currentTimeMillis();
        Hud.getDefault().playCongratulationSound();
    }

    public long getLastMoveDuration() {
        return System.currentTimeMillis() - this.lastPositionChange;
    }

    public void update() {
        if (this.metrics == null) {
            this.metrics = new MovingMetrics(System.currentTimeMillis(), this.vehicle.getPhysicsLocation());
        }
        if (!this.gameOver) {
            this.updateBodyEffects();
            this.updateArrow();
            this.updateMetrics();
            if (this.cam != null) {
                this.updateCam();
            }
            this.updateKeys();
            if (this.vehicle.getPhysicsLocation().y > 40) {
                this.updateWheelSkid();
                this.updateYeeHaa();
            }
        } else {
            if (System.currentTimeMillis() - this.gameOverSince > 5000 && this.vehicle != null) {
                this.parent.getRootNode().detachChild(this.vehicleNode);
                this.parent.getRootNode().detachAllChildren();
                this.parent.getPhysicsSpace().destroy();
                this.vehicle = null;
                this.audioBack.stop();
                this.audioGo.stop();
                this.audioHorn.stop();
                this.audioRun.stop();
                this.audioVelo.stop();

                this.parent.reInit();
            }
        }
    }

    public void updateYeeHaa() {
        int direction1 = this.metrics.getYbasedDirection(this.vehicleNode.getLocalRotation().getRotationColumn(2));
        if (!this.forward) {
            direction1 += 180;
            direction1 %= 360;
        }
        int direction2 = this.metrics.getDirection();
        if (direction1 > 270 && direction2 < 90) {
            direction1 -= 360;
        } else if (direction2 > 270 && direction1 < 90) {
            direction2 -= 360;
        }
        int directionDelta = Math.abs(direction2 - direction1);

        if (this.vehicle.getWheel(2).getSkidInfo() < 0.1 && directionDelta > 85 && this.metrics.getSpeed() > 18 && System.currentTimeMillis() - this.lastYehaw > 5000) {
            this.lastYehaw = System.currentTimeMillis();
//            System.out.println(">: YeeHaw / " + this.lastYehaw);
            this.audioYeehaw.play();
            this.turbo();
        }
    }

    public void updateWheelSkid() {
        float treshold = 0.1f;
        

        for (int i = 0; i < 4; ++i) {
            if (this.vehicle.getWheel(i).getSkidInfo() < treshold && this.vehicle.getWheel(i).getSkidInfo() > 0) {
                Vector3f position = this.vehicle.getWheel(i).getWheelSpatial().getWorldTranslation();
                position.y -= 0.4f;
                this.addDust(position);
            }
        }

        List<Long> remove = new ArrayList<>();
        for (long then : this.particleEmitterMap.keySet()) {
            long now = System.currentTimeMillis();
            if (now - then > 60000) {
                this.particleEmitterMap2.get(this.particleEmitterMap.get(then)).killAllParticles();
                this.parent.getRootNode().detachChild(this.particleEmitterMap.get(then));
                remove.add(then);
            }

        }
        for (long then : remove) {
            this.particleEmitterMap2.remove(this.particleEmitterMap.get(then));
            this.particleEmitterMap.remove(then);
        }
    }
    
    private void accelerateWheels() {
        this.vehicle.accelerate(0,this.accelerationValue);
        this.vehicle.accelerate(1,this.accelerationValue);

        this.vehicle.accelerate(2,this.accelerationValue);
        this.vehicle.accelerate(3,this.accelerationValue);
    }
    
    public void updateKeys() {
        float speed = Math.min(this.maxSpeed, this.getSpeed());
        float accelerationPower = .8f - speed / this.maxSpeed;
        float accelerationForce = accelerationPower * this.maxAccelerationForce;
        boolean accelratable = this.getSpeed() < this.maxSpeed;

        if (this.key[0][1]) {
            if (accelratable && accelerationPower >= .8) {
                this.turbo();
                this.audioVelo.play();
            }

            if (this.forward) {
                if (accelratable) {
                    this.accelerationValue += accelerationForce;
                    if (runPitch[1] < runPitch[2]) {
                        runPitch[1] += runPitch[3];
                    }
                    this.vehicle.brake(0);
                } else {
                    this.accelerationValue = 0;
                }
                this.accelerateWheels();
            } else {
                this.accelerationValue = 0;
                if (speed > 0) {
                    this.vehicle.brake(this.brakeForce);
                } else {
                    this.vehicle.brake(0f);
                    this.forward = true;
                    this.audioBack.stop();
                }
            }
        } else if (this.key[2][1]) {
            if (accelratable && accelerationPower >= .8) {
                this.turbo();
                this.audioVelo.play();
            }
            if (!this.forward) {
                if (accelratable) {
                    this.accelerationValue -= accelerationForce;
                    if (runPitch[1] < runPitch[2]) {
                        runPitch[1] += runPitch[3];
                    }
                    this.vehicle.brake(0);
                } else {
                    this.accelerationValue = 0;
                }
                this.accelerateWheels();
            } else {
                this.accelerationValue = 0;
                if (speed > 0) {
                    this.vehicle.brake(this.brakeForce);
                } else {
                    this.vehicle.brake(0f);
                    this.forward = false;
                    this.audioBack.play();
                }
            }
        } else {
            this.vehicle.accelerate(0);
            if (runPitch[1] > runPitch[0] + runPitch[3]) {
                runPitch[1] -= runPitch[3];
            }
            if (runPitch[1] > runPitch[0] + runPitch[3]) {
                runPitch[1] -= runPitch[3];
            }
        }

        this.audioRun.setPitch(this.runPitch[1]);
        int deltaType = 0;
        if (this.metrics.getSpeed() > 10) {
            deltaType = 1;
        }
        float antiSpeed = 101 - Math.min(this.metrics.getSpeed(), 100);
        float steerinDelta = antiSpeed / 1000f;
        
        if (this.key[1][0]) {
            if (this.currentSteeringValue < this.maxSteeringValue[deltaType]) {
//                this.currentSteeringValue += this.steeringDeltaValue[deltaType];
                this.currentSteeringValue += steerinDelta;
            }
            if (this.currentSteeringValue < 0) {
                this.currentSteeringValue = 0;
            }
            this.steering = -1;
        } else if (this.key[1][2]) {
            if (this.currentSteeringValue > -1 * this.maxSteeringValue[deltaType]) {
//                this.currentSteeringValue -= this.steeringDeltaValue[deltaType];
                this.currentSteeringValue -= steerinDelta;
            }
            if (this.currentSteeringValue > 0) {
                this.currentSteeringValue = 0;
            }
            this.steering = 1;
        } else {
            if (this.currentSteeringValue < 0) {
//                this.currentSteeringValue += this.steeringDeltaValue[deltaType];
                this.currentSteeringValue += steerinDelta;
            }
            if (this.currentSteeringValue > 0) {
//                this.currentSteeringValue -= this.steeringDeltaValue[deltaType];
                this.currentSteeringValue -= steerinDelta;
            }
//            if (this.currentSteeringValue > -1 * this.steeringDeltaValue[deltaType] && this.currentSteeringValue < this.steeringDeltaValue[deltaType]) {
            if (this.currentSteeringValue > -1 * steerinDelta && this.currentSteeringValue < this.steeringDeltaValue[deltaType]) {
                this.currentSteeringValue = 0;
            }
            this.steering = 0;
        }
        this.vehicle.steer(this.currentSteeringValue);

        this.key = new boolean[][]{
            {false, false, false},
            {false, false, false},
            {false, false, false}
        };
    }

    public void updateBodyEffects() {
        if (this.metrics != null) {
            int speed = this.metrics.getSpeed();
            int acceleration = this.metrics.getAcceleration();
            if (this.steering != 0 && speed > 5) {
                this.targetRoll = (float) (speed / 1.5 * this.bodyRotationFaktor * this.steering);
            } else {
                this.targetRoll = 0;
            }

//            System.out.println(this.steering + "*: " + this.targetRoll);
            if (Math.abs(acceleration) > 5 && speed < 30) {
                this.targetYaw = acceleration * this.bodyRotationFaktor;
            } else {
                this.targetYaw = 2;
            }

            if (!this.forward && speed > 3) {
                this.targetYaw *= -1;
                this.targetRoll *= -1;
            }

            if (this.curentYaw - 2 * this.bodyRotation < this.targetYaw) {
                this.curentYaw = this.curentYaw + this.bodyRotation;
            } else if (this.curentYaw + 2 * this.bodyRotation > this.targetYaw) {
                this.curentYaw = this.curentYaw - this.bodyRotation;
            }
            if (this.curentRoll - 2 * this.bodyRotation < this.targetRoll) {
                this.curentRoll = this.curentRoll + this.bodyRotation;
            } else if (this.curentRoll + 2 * this.bodyRotation > this.targetRoll) {
                this.curentRoll = this.curentRoll - this.bodyRotation;
            }

            float defYaw = this.curentYaw - this.yawRotation;
            float defRoll = this.curentRoll - this.rollRotation;

            this.body.rotate(
                    (float) (defYaw * Math.PI / 180.0),
                    0f,
                    (float) (defRoll * Math.PI / 180.0));

            this.yawRotation += defYaw;
            this.rollRotation += defRoll;

        }
    }

    public void updateArrow() {
        if (!this.gameOver) {
            if (this.target != null) {
                this.arrow.lookAt(this.target);
            }
        }
    }

   public void updateMetrics() {
        if (!this.gameOver) {
            if (this.vehicle.getPhysicsLocation().y < 1) {
                this.setGameOver();
            }
//            System.out.println(this.vehicle.getPhysicsLocation());
            if (this.metrics == null) {
                this.metrics = new MovingMetrics(System.currentTimeMillis(), this.vehicle.getPhysicsLocation());
            } else {
                this.metrics.addMessure(System.currentTimeMillis(), this.vehicle.getPhysicsLocation());
//                System.out.println(this.metrics.toString());
            }
        }
    }


    
    public void updateCam() {
        if (!this.gameOver) {
            if (!this.lastPositions.isEmpty()) {
                if (this.lastPositions.get(this.lastPositions.size() - 1).distance(this.vehicle.getPhysicsLocation()) > 0.05) {
//                    this.lastPositionChange = System.currentTimeMillis();
                    this.lastPositions.add(this.vehicle.getPhysicsLocation());
                }
                while (this.lastPositions.size() > 1 && this.lastPositions.get(0).distance(this.vehicle.getPhysicsLocation()) > 25) {
                    this.lastPositions.remove(0);
                }
                if (this.lastPositions.get(0).distance(this.vehicle.getPhysicsLocation()) > 20) {
                    this.camTarget = this.lastPositions.get(0);
                    Float offY = this.parent.getEnvironmentHeight(this.camTarget.x, this.camTarget.z);

                    if (offY != null) {
                        float distance = this.target.getPhysicsLocation().distance(this.vehicle.getPhysicsLocation());
                        if(this.target != null && distance < 30) {
//                            System.out.println(this.target.getPhysicsLocation().distance(this.vehicle.getPhysicsLocation()));
                            offY += (30 - distance) * 2.5F;
                        } 
                        this.camTarget.y = offY + 5f;
                    }
                    this.cam.setLocation(camTarget);
                    this.initCampos = true;
                }
            }
            this.cam.lookAt(this.vehicle.getPhysicsLocation(), Vector3f.UNIT_Y);
            if (!this.initCampos) {

                Float distance = this.camTarget.distance(this.vehicle.getPhysicsLocation());
                if (distance > 35) {
                    this.camTarget = this.vehicle.getPhysicsLocation();
                    this.camTarget.x += Math.random() * 10 - 5;
                    this.camTarget.z += Math.random() * 10 - 5;
                    Float offY = this.parent.getEnvironmentHeight(this.camTarget.x, this.camTarget.z);
                    
                    
                    if (offY != null) {
                        this.camTarget.y = offY + 1f;
                    }
                    if (Math.random() * 100 > 20) {
                        this.camTarget.y += Math.random() * 5 + 2;
                    }
                }

                Float camspeed = 0.006f * Math.max(35, distance);
                if (this.cam.getLocation().x < this.camTarget.x - (camspeed + .1f)) {
                    this.cam.getLocation().x += camspeed;
                } else if (this.cam.getLocation().x > this.camTarget.x + (camspeed + .1f)) {
                    this.cam.getLocation().x -= camspeed;
                }

                if (this.cam.getLocation().y < this.camTarget.y - (camspeed / 3 + .1f)) {
                    this.cam.getLocation().y += camspeed / 4;
                } else if (this.cam.getLocation().y > this.camTarget.y + (camspeed + .1f)) {
                    this.cam.getLocation().y -= camspeed / 4;
                }

                if (this.cam.getLocation().z < this.camTarget.z - (camspeed + .1f)) {
                    this.cam.getLocation().z += camspeed;
                } else if (this.cam.getLocation().z > this.camTarget.z + (camspeed + .1f)) {
                    this.cam.getLocation().z -= camspeed;
                }
            }
            SceneControll.getDefault().checkTarget(this.parent);
        }
    }

    public abstract Material getBodyTextureMaterial();

    public abstract Spatial getBodyModel();

    public abstract Vector3f getBodyScale();
    
    public abstract Material getWheelMaterial();

    public abstract Spatial getWhellModel();

    public abstract Vector3f getWheelScale();
    
    public abstract CompoundCollisionShape getCompoundCollisionShape();

    public abstract Vector3f getLocalBodyTranslation();

    

    public abstract int getWeight();

    public abstract float getStiffness();

    public abstract float getCompValue();

    public abstract float getDampValue();

    private void initJeep(Vector3f initPosition) {
        Material mat_body;
        mat_body = this.getBodyTextureMaterial();
        this.body = this.getBodyModel();
        this.body.setMaterial(mat_body);
        this.body.setShadowMode(RenderQueue.ShadowMode.Cast);

        CompoundCollisionShape compoundShape = this.getCompoundCollisionShape();

        //create vehicle node
        this.initPosition = initPosition;
        this.vehicleNode.setLocalTranslation(initPosition);
        this.vehicleNode.rotate(0, (float) Math.toRadians(this.rotateY), 0);
        

        this.vehicleNode.attachChild(this.body);
        this.body.setLocalTranslation(this.getLocalBodyTranslation());
        this.body.rotate(0, FastMath.PI, 0);
//        this.body.rotate((float) Math.toRadians(-2), 0, 0);
        Vector3f scale = this.getBodyScale();
        this.body.scale(scale.x, scale.y, scale.z);
        this.vehicle = new VehicleControl(compoundShape, this.getWeight());

        this.vehicleNode.addControl(vehicle);
        float stiffness = this.getStiffness();
        float compValue = this.getCompValue();
        float dampValue = this.getDampValue();      

        this.vehicle.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        this.vehicle.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        this.vehicle.setSuspensionStiffness(stiffness);
        this.vehicle.setMaxSuspensionForce(this.maxSuspensionForce);
        
        // Todo
        Vector3f wheelDirection = new Vector3f(0, -1, 0); // was 0, -1, 0
        Vector3f wheelAxle = new Vector3f(-1, 0, 0); // was -1, 0, 0
        float radius = 0.8f;
        float restLength = 0.3f;
        float yOff = 0.5f;
        float xOff = 1.5f;
        float zOff = 1.9f;
        this.vehicle.setFriction(2.4f);
        this.vehicle.setFrictionSlip(2.4f);
        this.rotation = this.vehicle.getPhysicsRotation();
        
        
        
        
        
        Material mat_wheel = this.getWheelMaterial();
        Node node1 = new Node("wheel 1 node");
        Spatial wheelfr = this.getWhellModel();
        wheelfr.setMaterial(mat_wheel);
        node1.attachChild(wheelfr);
        wheelfr.rotate(0, 0, FastMath.PI/2);
        System.out.println(parent.shouldBeonDesktop());
        if (!parent.shouldBeonDesktop()) {
            System.out.println("*");
            wheelfr.rotate(FastMath.PI, 0, 0);
        }
        if (parent.shouldBeonDesktop()) {
            wheelfr.setLocalTranslation(.3f, 0f, 0f);
        } else {
            wheelfr.setLocalTranslation(-.3f, 0f, 0f);
        }
        Vector3f wheelScale = this.getBodyScale();
       
        wheelfr.scale(wheelScale.x, wheelScale.y, wheelScale.z);
        wheelfr.setShadowMode(RenderQueue.ShadowMode.Cast);
        this.wheelMap.put(node1, wheelfr);
        this.vehicle.addWheel(node1, new Vector3f(-xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);
        Node node2 = new Node("wheel 2 node");
        Spatial wheelfl = this.getWhellModel();
        wheelfl.setMaterial(mat_wheel);
        node2.attachChild(wheelfl);
        wheelfl.rotate(0, 0, -FastMath.PI/2);
        if (!parent.shouldBeonDesktop()) {
            wheelfl.rotate(FastMath.PI, 0, 0);
        }
        if (parent.shouldBeonDesktop()) {
            wheelfl.setLocalTranslation(-.3f, 0f, 0f);
        } else {
            wheelfl.setLocalTranslation(.3f, 0f, 0f);
        }
//        wheelfl.setLocalTranslation(-.3f, 0f, 0f);
        wheelfl.scale(wheelScale.x, wheelScale.y, wheelScale.z);
        
        wheelfl.setShadowMode(RenderQueue.ShadowMode.Cast);
        this.wheelMap.put(node2, wheelfl);
        this.vehicle.addWheel(node2, new Vector3f(xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);
        Node node3 = new Node("wheel 3 node");
        Spatial wheelrr = this.getWhellModel();
        wheelrr.setMaterial(mat_wheel);
        node3.attachChild(wheelrr);
        wheelrr.rotate(0, 0, FastMath.PI/2);
        if (!parent.shouldBeonDesktop()) {
            wheelrr.rotate(FastMath.PI, 0, 0);
        }
        wheelrr.scale(wheelScale.x, wheelScale.y, wheelScale.z);
        wheelrr.setShadowMode(RenderQueue.ShadowMode.Cast);
        this.wheelMap.put(node3, wheelrr);
        vehicle.addWheel(node3, new Vector3f(-xOff, yOff, -0.2f - zOff),
                wheelDirection, wheelAxle, restLength, radius, false);
        Node node4 = new Node("wheel 4 node");
        Spatial wheelrl = this.getWhellModel();
        wheelrl.setMaterial(mat_wheel);
        node4.attachChild(wheelrl);
        wheelrl.rotate(0, 0, -FastMath.PI/2);
        if (!parent.shouldBeonDesktop()) {
            wheelrl.rotate(FastMath.PI, 0, 0);
        }
        wheelrl.scale(wheelScale.x, wheelScale.y, wheelScale.z);
        wheelrl.setShadowMode(RenderQueue.ShadowMode.Cast);
        this.wheelMap.put(node4, wheelrl);
        vehicle.addWheel(node4, new Vector3f(xOff, yOff, -0.2f - zOff),
                wheelDirection, wheelAxle, restLength, radius, false);
        this.vehicleNode.attachChild(node1);
        this.vehicleNode.attachChild(node2);
        this.vehicleNode.attachChild(node3);
        this.vehicleNode.attachChild(node4);
        this.arrow = new Arrow(this.parent, new Vector3f(0f, 3f, 0f));
        this.vehicleNode.attachChild(this.arrow.getSpatial());
    }

    public boolean isForward() {
        return this.forward;
    }

    public Node getVehicleNode() {
        return this.vehicleNode;
    }

    public VehicleControl getVehicleControl() {
        return this.vehicle;
    }

    public void steerLeft() {
        this.key[1][0] = true;
    }

    public void steerRight() {
        this.key[1][2] = true;
    }

    public void jump() {
        this.vehicle.applyImpulse(this.jumpForce, Vector3f.ZERO);
    }

    public void turbo() {
        if (this.forward) {
            this.vehicle.applyImpulse(this.vehicleNode.getLocalRotation().getRotationColumn(2).mult(100f), this.vehicleNode.getLocalRotation().getRotationColumn(2));
        } else {
            this.vehicle.applyImpulse(this.vehicleNode.getLocalRotation().getRotationColumn(2).mult(100f).negateLocal(), this.vehicleNode.getLocalRotation().getRotationColumn(2).negateLocal());
        }
    }

    public int getSpeed() {
        if (this.metrics != null) {
            return this.metrics.getSpeed();
        }
        return 0;
    }

    private void addDust(Vector3f location) {
        Node tmp = new Node();
        ParticleEmitter dust = new ParticleEmitter("dust effect", Type.Point, 1);
        tmp.attachChild(dust);
        this.parent.getRootNode().attachChild(tmp);
        dust.setNumParticles(1);
        dust.setLocalTranslation(location);
        dust.setFaceNormal(Vector3f.NAN);
        dust.setParticlesPerSec(1);
        dust.setMaterial(this.smokeMat);
        dust.setStartColor(ColorRGBA.White);
        dust.setEndColor(ColorRGBA.White);
        dust.setStartSize(0.1f);
        dust.setEndSize(0.1f);
        dust.setImagesX(1); // columns
        dust.setImagesY(1); // rows
        dust.setSelectRandomImage(false);
        dust.setLowLife(1f);
        dust.setHighLife(1f);
        dust.setRotateSpeed(0.5f);
        dust.emitAllParticles();
        this.particleEmitterMap.put(System.currentTimeMillis(), tmp);
        this.particleEmitterMap2.put(tmp, dust);
    }

    private void explode() {
        Material shockWaveMaterial = new Material(
                this.parent.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        shockWaveMaterial.setTexture("Texture",
                this.parent.getAssetManager().loadTexture("Effects/Explosion/shockwave.png"));

        ParticleEmitter explosion = new ParticleEmitter("explosion effect", Type.Triangle, 1);
        this.parent.getRootNode().attachChild(explosion);
        Vector3f location = this.vehicle.getPhysicsLocation();
        location.y = location.y + 1;
        explosion.setLocalTranslation(location);
        explosion.setFaceNormal(Vector3f.UNIT_Y);
        explosion.emitAllParticles();
        explosion.setParticlesPerSec(0);
        explosion.setMaterial(shockWaveMaterial);
        explosion.setStartColor(ColorRGBA.Blue);
        explosion.setEndColor(ColorRGBA.LightGray);

        explosion.setStartSize(5f);
        explosion.setEndSize(20f);

        explosion.setImagesX(1); // columns
        explosion.setImagesY(1); // rows
        explosion.setSelectRandomImage(false);

//        this.vehicle.removeWheel(0);
//        this.vehicle.removeWheel(0);
//        this.vehicle.removeWheel(0);
//        this.vehicle.removeWheel(0);
        Material mat_wheel = this.getWheelMaterial();
        for (Node node : this.wheelMap.keySet()) {
            Node tmp = new Node();
            tmp.setLocalTranslation(node.getWorldTranslation());
            tmp.setLocalRotation(node.getWorldRotation());
            this.vehicleNode.detachChild(node);
            this.wheelMap.get(node).setMaterial(mat_wheel);
            tmp.attachChild(this.wheelMap.get(node));
            this.parent.getRootNode().attachChild(tmp);

            RigidBodyControl rbc = new RigidBodyControl(1f);
            this.wheelMap.get(node).addControl(rbc);
            this.parent.getPhysicsSpace().add(rbc);
            if (this.forward) {
                rbc.applyImpulse(this.vehicleNode.getLocalRotation().getRotationColumn(2).mult(this.getSpeed()), this.vehicleNode.getLocalRotation().getRotationColumn(2));
            } else {
                this.vehicle.applyImpulse(this.vehicleNode.getLocalRotation().getRotationColumn(2).mult(this.getSpeed()).negateLocal(), this.vehicleNode.getLocalRotation().getRotationColumn(2).negateLocal());
            }
        }
        this.vehicle.applyImpulse(new Vector3f(1000, 20000, 100), Vector3f.ZERO);
        this.parent.reserViewport();
    }

    public void setGameOver() {
        if (!this.gameOver) {
            this.parent.setLooser(this);
            
            this.congratulation = false;
            this.gameOver = true;
            this.gameOverSince = System.currentTimeMillis();
            Hud.getDefault().playGameOverSound();
            this.explode();
        }
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public void accelerate() {
        if (!this.gameOver) {
            this.key[0][1] = true;
        }
    }

    public void shift() {
        if (System.currentTimeMillis() - this.lastPressed > 1000) {
            this.forward = !this.forward;
            this.lastPressed = System.currentTimeMillis();
        }
    }

    public void brake() {
        if (!this.gameOver) {
            this.key[2][1] = true;
        }
    }

    public void resetPosition() {
        this.audioGoal.play();
        vehicle.setPhysicsLocation(this.initPosition);
        vehicle.setPhysicsRotation(this.rotation);
        vehicle.setLinearVelocity(Vector3f.ZERO);
        vehicle.setAngularVelocity(Vector3f.ZERO);
        vehicle.resetSuspension();
          this.camTarget = this.initPosition.clone();
          this.camTarget.y += 75;
          this.cam.setLocation(camTarget);
    }

    private void put(Node node1, Spatial wheelfr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
