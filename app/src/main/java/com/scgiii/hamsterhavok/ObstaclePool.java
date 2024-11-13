package com.scgiii.hamsterhavok;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

//class recycles and draws obstacles from a pool to avoid egregious memory consumption
public class ObstaclePool {
    private List<Obstacle> activeObstacles = new ArrayList<>();
    private List<Obstacle> inactiveObstacles = new ArrayList<>();
    private ObstacleFactory factory;
    private Random random = new Random();

    public ObstaclePool(ObstacleFactory factory) {
        this.factory = factory;
    }

    public Obstacle obtainObstacle(float rightEdgeOfScreen) {
        Obstacle obstacle;

        //testing
        obstacle = factory.createRandomObstacle(rightEdgeOfScreen);

        if (inactiveObstacles.isEmpty() || random.nextFloat() < 0.3) { // 30% chance to create new
            obstacle = factory.createRandomObstacle(rightEdgeOfScreen);
        } else {
            obstacle = inactiveObstacles.remove(random.nextInt(inactiveObstacles.size()));
            factory.resetObstacle(obstacle, rightEdgeOfScreen);
        }
        activeObstacles.add(obstacle);
        return obstacle;
    }

    //recycling just removes an obstacle from the active list and places it in the inactive to be called upon later
    public void recycleObstacle(Obstacle obstacle) {
        activeObstacles.remove(obstacle);
        inactiveObstacles.add(obstacle);
    }

    //update is constantly running and going through list of active obstacles
    public void updateObstacles(float dt) {
        Iterator<Obstacle> iterator = activeObstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            //calls update on obstacles themselves(just moves them with the background)
            obstacle.update(dt);
            //removes and puts them in list of inactive obstacles when off screen
            if (obstacle.isOffScreen()) {
                iterator.remove();
                recycleObstacle(obstacle);
            }
        }
    }

    //draws every obstacle in the "active" list
    public void drawObstacles(Canvas canvas) {
        for (Obstacle obstacle : activeObstacles) {
            obstacle.draw(canvas);
        }
    }

    // ... other methods if needed ...

    /*public boolean checkCollision(Player player) {
        for (Obstacle obstacle : activeObstacles) {
            if (obstacle.collidesWith(player)) {
                return true;
            }
        }
        return false;
    }*/
}