package de.hc.jme.scene;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.MouseInput;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.util.SkyFactory;
import de.hc.jme.gui.controller.GuiController;
import de.hc.jme.gui.hud.Hud;
import de.hc.jme.jme.models.vehicle.AbstractVehicle;
import de.hc.jme.jme.models.vehicle.F40;
import de.hc.jme.jme.models.vehicle.F40Blue;
import de.hc.jme.jme.scene.controll.SceneControll;
import fe.hc.jme.models.Ball;
import fe.hc.jme.models.Ground;

public class F40Scene extends AbstractScene {
    private F40 f40; 
    private F40Blue f40_blue; 
    private Ball ball;
    private boolean firstInit = true;
    protected ViewPort view2;
    
    
    @Override
    public void reInit() {
        Hud.getDefault().setHide(false);
        Hud.getDefault().clean();
        this.simpleInitApp();
    }
    
    public void init(int islandIndex, boolean jeep) {
        this.memUp();
        Hud.getDefault().setHide(false);
        if (!this.firstInit) {
            this.rootNode.detachAllChildren();
            this.bulletAppState.cleanup();
            this.bulletAppState.getPhysicsSpace().destroy();
            this.bulletAppState.getPhysicsSpace().create();
            this.f40 = null;
            this.f40_blue = null;
            
            System.gc();
        } else {
            F40Scene.CURRENT = this;
            this.setUpLight();
            this.firstInit = false;
        }

        new Ground(this, new Vector3f(0, 100, 0));
        this.getRootNode().attachChild(SkyFactory.createSky(getAssetManager(), "Textures/Sky/Bright/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));        
        this.f40 = new F40(this, jeep, new Vector3f(0, 100, -50), 0);
        this.f40_blue = new F40Blue(this, jeep, new Vector3f(0, 100, 50), 180);
        this.ball = new Ball(this, new Vector3f(0, 100, 0), true);
        this.rootNode.attachChild(this.f40.getVehicleNode());
        this.rootNode.attachChild(this.f40_blue.getVehicleNode());
        this.rootNode.attachChild(this.ball.getNode());
        this.getPhysicsSpace().add(this.f40.getVehicleControl());
        this.getPhysicsSpace().add(this.f40_blue.getVehicleControl());
        this.f40.setCam(cam);
        
        
        Camera cam2 = this.cam.clone();
        this.cam.setViewPort( 0.0f , 1f, 0.0f, 0.5f);
        cam2.setViewPort( 0.0f , 1f, 0.51f, 1f);
        cam2.resize(this.settings.getWidth(), this.settings.getHeight(), true);
        cam.resize(this.settings.getWidth(), this.settings.getHeight(), true);
        
        
        
//        float ratio = this.settings.getWidth() / (this.settings.getHeight() / 2);
//        
//        cam2.setFrustumPerspective(cam.getFr, ratio, this.cam.getFrustumNear(), this.cam.getFrustumNear());
        this.view2 = renderManager.createMainView("View of camera 2", cam2);
        this.view2.setEnabled(true);
        this.view2.attachScene(this.rootNode);
        this.view2.setBackgroundColor(ColorRGBA.Blue);
        this.view2.setClearFlags(false, true, false);
        
        
        
        this.f40_blue.setCam(cam2);
        this.f40.setTarget(this.ball.getTarget());
        this.f40_blue.setTarget(this.ball.getTarget());
        
        SceneControll.getDefault().startGame(this);
    }
    @Override
    public void simpleInitApp() {
        if (this.guiController == null) {
            F40Scene.CURRENT = this;
            this.setupKeys();
            Hud.getDefault().setParent(this);
            this.flyCam.setEnabled(false);
            this.bulletAppState = new BulletAppState();
            this.stateManager.attach(bulletAppState);
            this.bulletAppState.setDebugEnabled(false);        
            this.guiController = new GuiController();
        } else {
            this.guiController.start();
        }
    }
    

    private final AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String binding, float value, float tpf) {
            if (binding.equals("Lefts")) {
                f40.steerLeft();
            } 
            if (binding.equals("Rights")) {
                f40.steerRight(); 
            }
            if (binding.equals("Ups")) {
                f40.accelerate();
            }
            if (binding.equals("Downs")) {
               f40.brake();
            }
            if (binding.equals("Space")) {
               f40.horn();
            }
            if (binding.equals("Reset")) {
               f40.resetPosition();
            }
            if (binding.equals("Left2")) {
                f40_blue.steerLeft();
            } 
            if (binding.equals("Right2")) {
                f40_blue.steerRight(); 
            }
            if (binding.equals("Up2")) {
                f40_blue.accelerate();
            }
            if (binding.equals("Down2")) {
               f40_blue.brake();
            }
            if (binding.equals("Space2")) {
               f40_blue.horn();
            }
//            if (binding.equals("Reset")) {
//               f40.turbo();
//            }
            if (binding.equals("Gear")) {
               f40.shift();
            }



            if (binding.equals("Touch")) {
               Hud.getDefault().touch();
            }
        }
    };

    
    private void setupKeys() {
        this.inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_LEFT));
        this.inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_RIGHT));
        this.inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_UP));
        this.inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_DOWN));
        this.inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        this.inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
        this.inputManager.addMapping("Gear", new KeyTrigger(KeyInput.KEY_RCONTROL));
        this.inputManager.addMapping("Left2", new KeyTrigger(KeyInput.KEY_A));
        this.inputManager.addMapping("Right2", new KeyTrigger(KeyInput.KEY_D));
        this.inputManager.addMapping("Up2", new KeyTrigger(KeyInput.KEY_W));
        this.inputManager.addMapping("Down2", new KeyTrigger(KeyInput.KEY_S));
        this.inputManager.addMapping("Space2", new KeyTrigger(KeyInput.KEY_LCONTROL));       
        this.inputManager.addMapping("Touch", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        this.inputManager.addListener(this.analogListener, "Lefts");
        this.inputManager.addListener(this.analogListener, "Rights");
        this.inputManager.addListener(this.analogListener, "Ups");
        this.inputManager.addListener(this.analogListener, "Downs");
        this.inputManager.addListener(this.analogListener, "Space");
        this.inputManager.addListener(this.analogListener, "Left2");
        this.inputManager.addListener(this.analogListener, "Right2");
        this.inputManager.addListener(this.analogListener, "Up2");
        this.inputManager.addListener(this.analogListener, "Down2");
        this.inputManager.addListener(this.analogListener, "Space2");
        this.inputManager.addListener(this.analogListener, "Reset");
        this.inputManager.addListener(this.analogListener, "Gear");
        this.inputManager.addListener(this.analogListener, "Touch");
    }
    
//    public F40 getJeep(){
//        return this.f40;
//    }

    @Override
    public void simpleUpdate(float tpf) {
        if (this.f40 != null && this.f40_blue != null) {
            this.f40.update();
            this.f40_blue.update();
            Hud.getDefault().update();
        } else {
            Hud.getDefault().clean();
        }
    }
    
    public boolean shouldBeonDesktop() {
        return false;
    }

    @Override
    public int getIsle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractVehicle[] getVehicles() {
        return new AbstractVehicle[] {this.f40, this.f40_blue}; 
    }

    @Override
    public void setLooser(AbstractVehicle looser) {
        if (looser.equals(this.f40)) {
            this.f40_blue.setCongratulation();
        } else {
            this.f40.setCongratulation();
        }

    }
    
    public void resetPositions() {
        this.f40.resetPosition();
        this.f40_blue.resetPosition();
        this.ball.Replace();
    }
    
    @Override
    public Ball getBall() {
        return this.ball;
    }

}