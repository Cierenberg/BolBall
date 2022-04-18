/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.jme.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import de.hc.jme.gui.controller.GuiController;
import de.hc.jme.jme.models.vehicle.AbstractVehicle;
import de.hc.jme.jme.utility.Utility;
import fe.hc.jme.models.Ball;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hendrik
 */
public abstract class AbstractScene extends SimpleApplication{
    protected BulletAppState bulletAppState;
    protected static AbstractScene CURRENT;
        
    protected GuiController guiController;    
    

    
    
    public static AbstractScene getCurrent() {
        return AbstractScene.CURRENT;
    }

    public GuiController getGuiController() {
        return this.guiController;
    }
    
    protected void setUpLight() {
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.04f));
        this.rootNode.addLight(ambient);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        this.rootNode.addLight(sun);  
    }  

    public void memUp() {
        try
    {
        Class c = Class.forName("java.nio.Bits");
        Field maxMemory;
        try
        {
            maxMemory = c.getDeclaredField("maxMemory"); // java < 11 
        }
        catch(NoSuchFieldException ex)
        {
            maxMemory = c.getDeclaredField("MAX_MEMORY"); // java >= 11
        }
        maxMemory.setAccessible(true);
        Field reservedMemory = c.getDeclaredField("reservedMemory");
        reservedMemory.setAccessible(true);
        synchronized (c) {
            maxMemory.setLong(null, 1610612736L);
            Long maxMemoryValue = (Long)maxMemory.get(null);
            Long reservedMemoryValueLong=0L;
            if(reservedMemory.get(null) instanceof java.util.concurrent.atomic.AtomicLong)
            {
                java.util.concurrent.atomic.AtomicLong reservedMemoryValue = (java.util.concurrent.atomic.AtomicLong)reservedMemory.get(null);
                reservedMemoryValueLong=reservedMemoryValue.longValue();
            }
            else
            {
                reservedMemoryValueLong=(Long)reservedMemory.get(null);
            }
            
            System.out.println("Memory: " + reservedMemoryValueLong + " / " + maxMemoryValue );
        }
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(AbstractScene.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchFieldException ex) {
        Logger.getLogger(AbstractScene.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SecurityException ex) {
        Logger.getLogger(AbstractScene.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalArgumentException ex) {
        Logger.getLogger(AbstractScene.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        Logger.getLogger(AbstractScene.class.getName()).log(Level.SEVERE, null, ex);
    }
        
        
        
    }
    
    @Override
    public AssetManager getAssetManager() {
        return this.assetManager;
    }
    
    public BulletAppState getBulletAppState() {
        return this.bulletAppState;
    }
    
    public PhysicsSpace getPhysicsSpace(){
        return bulletAppState.getPhysicsSpace();
    }

    public Geometry getGeometry(Spatial spatial){
        return Utility.getGeometryFromSpatial(spatial);
    }

        public AppSettings getAppSettings(){
        return this.settings;
    }
    
    
    public Float getEnvironmentHeight(Float x, Float z) {
        Vector3f location = new Vector3f(x, 1500, z);
        Vector3f direction = new Vector3f(0, -1, 0);
        Vector3f highestCut = Utility.firstRayCut(location, direction, this.rootNode);
        if (highestCut != null) {
            return highestCut.y;
        }
        return null;
    }

    @Override
    public abstract void simpleUpdate(float tpf);
    
    
    public abstract boolean shouldBeonDesktop();
    
    public void reserViewport() {
        this.cam.setViewPort( 0.0f , 1f, 0.0f, 1f);
    }
    
    // Project Specials
    
    public abstract void init(Ball.TYPE type, Ball.SIZE size);

    public abstract void reInit();
    
    public abstract int getIsle();
    
    public abstract AbstractVehicle[] getVehicles();
    
    public abstract void setLooser(AbstractVehicle looser);
    
    public abstract Ball getBall();
    
    public abstract void resetPositions();
}
