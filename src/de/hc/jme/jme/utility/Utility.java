/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.jme.jme.utility;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author hendrik
 */
public class Utility {
    public static void setTextureScale(Spatial spatial, Vector2f vector) {
        if (spatial instanceof Node) {
            Node findingnode = (Node) spatial;
            for (int i = 0; i < findingnode.getQuantity(); i++) {
                Spatial child = findingnode.getChild(i);
                Utility.setTextureScale(child, vector);
            }
        } else if (spatial instanceof Geometry) {
            ((Geometry) spatial).getMesh().scaleTextureCoordinates(vector);
        }
    }
        
    public static Vector3f firstRayCut(Vector3f start, Vector3f direction, Spatial spatial) {
         Ray ray = new Ray(start, direction);
        CollisionResults results = new CollisionResults();        
        results.clear();
        spatial.collideWith(ray, results);
        if (results.size() > 0) {
            CollisionResult result = results.getClosestCollision();
            return result.getContactPoint();
        }
        return null;
    }
    
    public static Spatial firstRayCut(Vector3f start, Vector3f direction, Spatial[] spatial) {
        Vector3f[] locations = {Utility.firstRayCut(start, direction, spatial[0]), Utility.firstRayCut(start, direction, spatial[1])};
        if (locations[0] != null && locations[1] != null) {
            if (start.distance(locations[0]) < start.distance(locations[1])) {
                return spatial[0];
            } else {
                return spatial[1];
            }
        }
        return null;
    }

    public static boolean isfirstRayCut(Vector3f start, Vector3f direction, Node node, String spatial) {
        try {
            Ray ray = new Ray(start, direction);
            CollisionResults results = new CollisionResults();        
            results.clear();
            node.collideWith(ray, results);
            if (results.size() > 0) {
                if (results.size() == 2 || results.getCollision(0).toString().contains(spatial) || results.getCollision(0).toString().contains("arrow") || results.getCollision(0).toString().contains("wheel")  || results.getCollision(0).toString().contains("ball")) {
                    return true;
                }
            }
            return false;
        } catch (Throwable t) {
           return true;  
        }
    }
        

    public static Vector3f firstRayCut(Vector3f start, Vector3f direction, Node node) {
        Ray ray = new Ray(start, direction);
        CollisionResults results = new CollisionResults();        
        results.clear();
        node.collideWith(ray, results);
        if (results.size() > 0) {
            CollisionResult result = results.getClosestCollision();
            return result.getContactPoint();
        }
        return null;
    }

//    public static Vector3f firstRayCut(Vector3f start, Vector3f direction, PhysicsSpace physicsSpace) {
//         Ray ray = new Ray(start, direction);
//        CollisionResults results = new CollisionResults();        
//        results.clear();
//        physicsSpace.collideWith(ray, results);
//        if (results.size() > 0) {
//            CollisionResult result = results.getClosestCollision();
//            return result.getContactPoint();
//        }
//        return null;
//    }
    
    public static Geometry getGeometryFromSpatial(Spatial spatial){
            Node node = (Node)spatial;
            Geometry g = null;

            final List<Spatial> ants = new LinkedList<>();
            node.depthFirstTraversal(new SceneGraphVisitor() {
                    @Override
                    public void visit(Spatial spatial) {
                            if (spatial.getClass().getName().equals("com.jme3.scene.Geometry")) {
                                    ants.add(spatial);
                            }
                    }
            });
            if (!ants.isEmpty()) {
                    for (int i = 0;i < ants.size();i++){
                            if (ants.get(i).getClass().getName().equals("com.jme3.scene.Geometry")){
                                    g = (Geometry)ants.get(i);
                                    return(g);
                            }
                    }
            }
            else
            {
                    System.out.println("Geometry not found");
            }
            return(g);
    }
}
