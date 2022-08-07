package de.hc.jme.gui.controller;



import com.jme3.niftygui.NiftyJmeDisplay;
import de.hc.jme.gui.hud.Hud;
import de.hc.jme.scene.F40Scene;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.RadioButton;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fe.hc.jme.models.Ball;


public class GuiController implements ScreenController {
    
    private Nifty nifty;
    private boolean visible = true;
    
    public GuiController() {
        super();
        this.init();
    }
    
    
    private void init() {
        Hud.getDefault().setHide(true);
        Hud.getDefault().clean();
        
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(F40Scene.getCurrent().getAssetManager(), 
        F40Scene.getCurrent().getInputManager(), 
        F40Scene.getCurrent().getAudioRenderer(), 
        F40Scene.getCurrent().getViewPort());
        this.nifty = niftyDisplay.getNifty();
        
        F40Scene.getCurrent().getViewPort().addProcessor(niftyDisplay);
        this.nifty.fromXml("Interface/screen.xml", "start", this);
//        this.nifty.getScreen("start").findNiftyControl("cb2", RadioButton.class).select();
//        this.nifty.getScreen("start").findNiftyControl("cb4", RadioButton.class).select();
//        AbstractScene.getCurrent().init(1, true);
    }
    
    public void start() {
        Hud.getDefault().setHide(true);
        Hud.getDefault().clean();
        this.visible = true;
        System.out.println(this.visible);
        this.nifty.gotoScreen("start");
    }
    
    public void startGame(String nextScreen) {
        Screen screen = this.nifty.getScreen("start");
        Ball.TYPE ballType = Ball.TYPE.RUGBY;
        Ball.SIZE ballSize = Ball.SIZE.SMALL;
        if (screen.findNiftyControl("cb2", RadioButton.class).isActivated()) {
            ballType = Ball.TYPE.SOCCER;
        }
        if (screen.findNiftyControl("cb3", RadioButton.class).isActivated()) {
            ballType = Ball.TYPE.PUC;
        }
        if (screen.findNiftyControl("cb5", RadioButton.class).isActivated()) {
            ballSize = Ball.SIZE.BIG;
        }
        this.nifty.gotoScreen("hud");
        this.visible = false;
        F40Scene.getCurrent().init(ballType, ballSize);
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void quitGame() {
        System.exit(0);
    }
    
    public void resetGui() {
        this.start();
    }
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
}

