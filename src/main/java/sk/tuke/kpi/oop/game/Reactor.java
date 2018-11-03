package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.actions.PerpetualReactorHeating;
import sk.tuke.kpi.oop.game.tools.Hammer;

public class Reactor extends AbstractActor {
    private int temperature;
    private int damage;
    private Animation normalAnimation;
    private Animation hotAnimation;
    private Animation brokenAnimation;
    private Animation turnedOffAnimation;
    private boolean isRunning;
    private Light light;

    public Reactor() {
        temperature = 0;
        damage = 0;

        normalAnimation = new Animation(
            "sprites/reactor_on.png",
            80,
            80,
            0.1f,
            Animation.PlayMode.LOOP_PINGPONG);

        hotAnimation = new Animation(
            "sprites/reactor_hot.png",
            80,
            80,
            0.05f,
            Animation.PlayMode.LOOP_PINGPONG);

        brokenAnimation = new Animation(
            "sprites/reactor_broken.png",
            80,
            80,
            0.1f,
            Animation.PlayMode.LOOP_PINGPONG);

        turnedOffAnimation = new Animation(
            "sprites/reactor.png",
            80,
            80
        );


        turnOff();
    }

    public int getTemperature() {
        return temperature;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void turnOn() {
        if (damage == 100) {
            return;
        }
        isRunning = true;

        if (light != null) {
            light.setElectricityFlow(isRunning);
        }

        updateAnimation();
    }

    public void turnOff() {
        isRunning = false;

        if (light != null) {
            light.setElectricityFlow(isRunning);
        }

        updateAnimation();
    }

    public void increaseTemperature(int increment) {
        if(damage == 100 || !isRunning)
            return;

        double multiplier;
        if(damage < 33) {
            multiplier = 1;
        } else if (damage <= 66) {
            multiplier = 1.5;
        } else {
            multiplier = 2;
        }

        temperature += Math.ceil(multiplier * increment);

        double minInterval = 2000;
        double maxInterval = 6000;

        if (temperature > minInterval && damage < 100) {
            double tempDamage = (temperature - minInterval) / (maxInterval - minInterval) * 100.0;
            if (tempDamage < 100) {
                damage = (int)tempDamage;
                updateAnimation();
            } else {
                damage = 100;
                turnOff();
            }
        }
    }

    public void decreaseTemperature(int decrement) {
        if (damage == 100 || !isRunning || temperature == 0)
            return;

        temperature -= damage < 50 ? decrement : 0.5 * decrement;
        if (temperature < 0) {
            temperature = 0;
        }

        updateAnimation();
    }

    private void updateAnimation() {
        if (damage == 100) {
            setAnimation(brokenAnimation);
        } else if (!isRunning) {
            setAnimation(turnedOffAnimation);
        } else if (damage > 50) {
            setAnimation(hotAnimation);
        } else {
            setAnimation(normalAnimation);
        }
    }

    public void repairWith(Hammer hammer) {
        if (hammer == null || damage == 0 || damage == 100) {
            return;
        }

        hammer.use();

        double minInterval = 2000;
        double maxInterval = 6000;

        int tempDamage = damage - 50;
        damage = tempDamage > 0 ? tempDamage : 0;

        int tempTemperature = (int)(((maxInterval - minInterval) * tempDamage / 100 ) + minInterval);
        if (tempTemperature < temperature) {
            temperature = tempTemperature;
        }

        updateAnimation();

    }

    public void addLight(Light light) {
        this.light = light;
        this.light.setElectricityFlow(isRunning);
    }

    public void removeLight() {
        light = null;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new PerpetualReactorHeating(1).scheduleOn(this);
    }
}
