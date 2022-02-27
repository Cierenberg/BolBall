package mygame;
import com.jme3.system.AppSettings;
import de.hc.jme.scene.F40Scene;

public class Main extends F40Scene {
    public static void main(String[] args) {
        F40Scene app = (F40Scene) new Main();
        app.setShowSettings(false);
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(30);
        app.setSettings(settings);
        app.setDisplayStatView(false);
        app.setDisplayFps(false);
        app.start();
    }
}