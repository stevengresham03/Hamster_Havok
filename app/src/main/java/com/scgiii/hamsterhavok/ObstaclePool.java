/*





The idea of this class was to better manage and recycle our bitmaps. Was too hard to implememt though; kept getting errors.
Anyone is welcome to try again tho- Steven











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

    private static final int MAX_POOL_SIZE = 20;

    public ObstaclePool(ObstacleFactory factory) {
        this.factory = factory;
    }

    public Obstacle obtainObstacle(float rightEdgeOfScreen) {
        Obstacle obstacle;
        if ((inactiveObstacles.isEmpty() || random.nextFloat() < 0.3)) { // 30% chance to create new

            obstacle = factory.createRandomObstacle(rightEdgeOfScreen);

       } else {
            obstacle = inactiveObstacles.remove(0);
            //factory.resetObstacle(obstacle, rightEdgeOfScreen);
        }
        activeObstacles.add(obstacle);
        return obstacle;
    }

    //recycling just removes an obstacle from the active list and places it in the inactive to be called upon later
    public void recycleObstacle(Obstacle obstacle) {
        activeObstacles.remove(obstacle);
        if (inactiveObstacles.size() < MAX_POOL_SIZE) {
            inactiveObstacles.add(obstacle);
        }
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
        //Log.d("Pool", "Drawing obstacles");
        for (Obstacle obstacle : activeObstacles) {
            obstacle.draw(canvas);
        }
    }

    public void prewarmPool(int count, float rightEdgeOfScreen) {
        for (int i = 0; i < count; i++) {
            inactiveObstacles.add(factory.createRandomObstacle(rightEdgeOfScreen));
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
    }
}*/