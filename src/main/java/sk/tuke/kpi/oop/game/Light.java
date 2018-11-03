package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor {

    private Animation onAnimation;
    private Animation offAnimation;
    private boolean isOn;
    private boolean isPowered;

    public Light() {
        onAnimation = new Animation("sprites/light_on.png", 16, 16);
        offAnimation = new Animation("sprites/light_off.png", 16, 16);
        setAnimation(offAnimation);
        isOn = isPowered = false;

    }

    public void toggle() {
        if(!isOn && isPowered) {
            setAnimation(onAnimation);
            isOn = true;
        } else {
            setAnimation(offAnimation);
            isOn = false;
        }
    }

    public void setElectricityFlow(boolean isPowered) {
        this.isPowered = isPowered;
        updateAnimation();
    }

    private void updateAnimation() {
        if(isOn && isPowered) {
            setAnimation(onAnimation);
        } else {
            setAnimation(offAnimation);
        }
    }

}
