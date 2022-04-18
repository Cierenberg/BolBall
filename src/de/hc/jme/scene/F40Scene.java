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
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.util.SkyFactory;
import de.hc.jme.gui.controller.GuiController;
import de.hc.jme.gui.hud.Hud;
import de.hc.jme.jme.models.vehicle.AbstractVehicle;
import de.hc.jme.jme.models.vehicle.F40;
import de.hc.jme.jme.models.vehicle.Lambo;
import de.hc.jme.jme.scene.controll.SceneControll;
import fe.hc.jme.models.Ball;
import fe.hc.jme.models.Barrel;
import fe.hc.jme.models.Ground;
import fe.hc.jme.models.Loop;

public class F40Scene extends AbstractScene {
//    private F40 f40; 
//    private Lambo lambo; 
//    protected DirectionalLightShadowRenderer dlsr2;
    private AbstractVehicle f40; 
    private AbstractVehicle lambo; 
    private Ball ball;
    private Ground ground;
    private Camera cam2;
    private boolean firstInit = true;
    protected ViewPort view2;
    
    
    @Override
    public void reInit() {
        Hud.getDefault().setHide(false);
        Hud.getDefault().clean();
        this.simpleInitApp();
    }
    
    public void init(Ball.TYPE type, Ball.SIZE size) {
        SceneControll.getDefault().resetPoints();
        if (!this.shouldBeonDesktop()) {
            this.memUp();
        }
        Hud.getDefault().setHide(false);
        if (!this.firstInit) {
            this.rootNode.detachAllChildren();
            this.bulletAppState.cleanup();
            this.bulletAppState.getPhysicsSpace().destroy();
            this.bulletAppState.getPhysicsSpace().create();
            this.f40 = null;
            this.lambo = null;
            this.ball = null;
            this.ground = null;
            this.cam2 = null;
            this.view2 = null;
            System.gc();
        } else {
            F40Scene.CURRENT = this;
            this.setUpLight();
            this.firstInit = false;
        }

        this.ground = new Ground(this, new Vector3f(0, 100, 0));
        new Loop(this, new Vector3f(40, 116, 20));
        
        for (int i = 0; i < 11; i++) {
            float x = (float) (Math.random() * 80f - 40f);
            float z = (float) (Math.random() * 100f - 50f);
            System.out.println(x + "/" + z);
            this.getRootNode().attachChild(new Barrel(this, new Vector3f(x, 120, z), false, true).getNode());
            
        }
        
        this.getRootNode().attachChild(SkyFactory.createSky(getAssetManager(), "Textures/Sky/Bright/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));        
        this.f40 = new F40(this, new Vector3f(0, 100, -50), 0);
        this.lambo = new Lambo(this,new Vector3f(0, 100, 50), 180);
        this.ball = new Ball(this, new Vector3f(0, 100, 0), true, type, size);
        this.rootNode.attachChild(this.f40.getVehicleNode());
        this.rootNode.attachChild(this.lambo.getVehicleNode());
        this.rootNode.attachChild(this.ball.getNode());
        this.getPhysicsSpace().add(this.f40.getVehicleControl());
        this.getPhysicsSpace().add(this.lambo.getVehicleControl());
        this.f40.setCam(cam);
        
        this.cam2 = this.cam.clone();
        this.cam.setViewPort( 0.0f , 1f, 0.0f, 0.5f);
        this.cam2.setViewPort( 0.0f , 1f, 0.51f, 1f);
        this.cam2.resize(this.settings.getWidth(), this.settings.getHeight(), true);
        this.cam.resize(this.settings.getWidth(), this.settings.getHeight(), true);
        
        this.view2 = renderManager.createMainView("View of camera 2", this.cam2);
        this.view2.setEnabled(true);
        this.view2.attachScene(this.rootNode);
        this.view2.setBackgroundColor(ColorRGBA.Blue);
        this.view2.setClearFlags(false, true, false);
//        if (this.dlsr2 != null) {
//            this.view2.addProcessor(this.dlsr2);
//        }
        
        this.lambo.setCam(cam2);
        this.f40.setTarget(this.ball.getTarget());
        this.lambo.setTarget(this.ball.getTarget());
        
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
                lambo.steerLeft();
            } 
            if (binding.equals("Right2")) {
                lambo.steerRight(); 
            }
            if (binding.equals("Up2")) {
                lambo.accelerate();
            }
            if (binding.equals("Down2")) {
               lambo.brake();
            }
            if (binding.equals("Space2")) {
               lambo.horn();
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
        if (this.f40 != null && this.lambo != null) {
            this.f40.update();
            this.lambo.update();
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
        return new AbstractVehicle[] {this.f40, this.lambo}; 
    }

    @Override
    public void setLooser(AbstractVehicle looser) {
        if (looser.equals(this.f40)) {
            this.lambo.setCongratulation();
        } else {
            this.f40.setCongratulation();
        }

    }
    
    public void resetPositions() {
        this.f40.resetPosition();
        this.lambo.resetPosition();
        this.ball.Replace();
    }
    
    @Override
    public Ball getBall() {
        return this.ball;
    }
    

}