/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.jme.scene;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
 *
 * @author hendrik
 */
public class F40SceneDesktop extends F40Scene{
    /**
     * Inits the display.
     *
     * @return the app settings
     */
    private static AppSettings initDisplay() {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] modes = device.getDisplayModes();
//        DisplayMode biggest = modes[modes.length - 1];
        DisplayMode biggest = modes[3];
        AppSettings settings = new AppSettings(true);
        settings.put("Width", biggest.getWidth());
        settings.put("Height", biggest.getHeight());
        settings.put("Title", "BolBall");
        for (DisplayMode m : modes) {
            System.out.println(m.getWidth() + "/" + m.getHeight());
        }
        settings.setFrameRate(30);
        return settings;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        F40Scene.CURRENT = (F40Scene) new F40SceneDesktop();
        F40Scene.CURRENT.setShowSettings(false);
        F40Scene.CURRENT.setSettings(F40SceneDesktop.initDisplay());
        F40Scene.CURRENT.start();
        System.out.println("######");
    }
 
    /* (non-Javadoc)
     * @see de.hc.custom.scene.PoolScene#setUpLight()
     */
    @Override
    protected void setUpLight() {
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.04f));
        this.rootNode.addLight(ambient);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        this.rootNode.addLight(sun);

        final int SHADOWMAP_SIZE = 512;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
//        this.dlsr2 = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        dlsr.setShadowIntensity(0.7f);
//        this.dlsr2.setLight(sun);
//        this.dlsr2.setShadowIntensity(0.7f);
        this.viewPort.addProcessor(dlsr);
    }
    
    /* (non-Javadoc)
     * @see de.hc.custom.scene.PoolScene#shouldBeonDesktop()
     */
    @Override
    public boolean shouldBeonDesktop() {
        return true;
    }   
}
