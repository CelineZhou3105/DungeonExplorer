package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.animation.Timeline;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class Hound extends Entity{
    private IntegerProperty start_x, start_y;
    private IntegerProperty end_x, end_y; 
    private Timeline timeline;
    private String towards;

    public Hound(Dungeon d, int x, int y, int end_x, int end_y) {
        super(x, y, "hound");
        this.start_x = new SimpleIntegerProperty(x);
        this.start_y = new SimpleIntegerProperty(y);
        this.end_x = new SimpleIntegerProperty(end_x);
        this.end_y = new SimpleIntegerProperty(end_y);

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame key = new KeyFrame(Duration.millis(700), event -> {
            houndMove(d);
        });
        timeline.getKeyFrames().add(key);
        this.towards = "end";
        timeline.play();
    }

    public void houndMove(Dungeon d) {
        // Check if any obstacles are around
        ArrayList<Entity> left = d.checkOccupied(x().get() - 1, y().get());
        ArrayList<Entity> right = d.checkOccupied(x().get() + 1, y().get());
        ArrayList<Entity> up = d.checkOccupied(x().get(), y().get() - 1);
        ArrayList<Entity> down = d.checkOccupied(x().get(), y().get() + 1);

        Player p = d.getPlayer();

        // Will pace back and forth indefinitely until player is killed or hound is killed
        if (towards == "start") {
            if (x().get() < start_x.get()) {
                if (right.contains(p)) {
                    collide(p);
                    x().set(x().get() + 1);
                } else if (right.size() == 0) { // nothing else there
                    x().set(x().get() + 1);
                } else {
                    switchDirection();
                }
            } else if (x().get() > start_x.get()) {
                if (left.contains(p)) {
                    collide(p);
                    x().set(x().get() - 1);
                } else if (left.size() == 0) {
                    x().set(x().get() - 1);
                } else {
                    switchDirection();
                }
            } else if (y().get() < start_y.get()) {
                if (down.contains(p)) {
                    collide(p);
                    y().set(y().get() + 1);
                } else if (down.size() == 0) { 
                    y().set(y().get() + 1);
                } else {
                    switchDirection();
                }
            } else if (y().get() > start_y.get()) {
                if (up.contains(p)) {
                    collide(p);
                    y().set(y().get() - 1);
                } else if (up.size() == 0) {
                    y().set(y().get() - 1);
                } else {
                    switchDirection();
                }
                
            }
        } else {
            if (x().get() < end_x.get()) { // Move right
                if (right.contains(p)) {
                    collide(p);
                    x().set(x().get() + 1);
                } else if (right.size() == 0) { // nothing else there
                    x().set(x().get() + 1);
                } else {
                    switchDirection();
                }
                
            } else if (x().get() > end_x.get()) { // Move left
                if (left.contains(p)) {
                    collide(p);
                    x().set(x().get() - 1);
                } else if (left.size() == 0) {
                    x().set(x().get() - 1);
                } else {
                    switchDirection();
                }
                
            } else if (y().get() < end_y.get()) { // Move down
                if (down.contains(p)) {
                    collide(p);
                    y().set(y().get() + 1);
                } else if (down.size() == 0) { 
                    y().set(y().get() + 1);
                } else {
                    switchDirection();
                }
                
            } else if (y().get() > end_y.get()) { // Move up
                if (up.contains(p)) {
                    collide(p);
                    y().set(y().get() - 1);
                } else if (up.size() == 0) {
                    y().set(y().get() - 1);
                } else {
                    switchDirection();
                }
            }
        }

        if (x().get() == end_x.get() && y().get() == end_y.get()) {
            // We've reached the end
            towards = "start";
        } else if (x().get() == start_x.get() && y().get() == start_y.get()) {
            // We've reached the start
            towards = "end";
        }

    }

    public void switchDirection() {
        if (towards.equals("start")) {
            towards = "end";
        } else {
            towards = "start";
        }
    }

    /**
     * Controls collision behaviour between player and hound.
     * @param p player
     */
    @Override
    public void collide(Player p) {
        if (p.isLethal()) {
            // Enemy dies
            p.killEnemy(this);
            timeline.stop();
        } else {
            p.die();
        }
    }
}