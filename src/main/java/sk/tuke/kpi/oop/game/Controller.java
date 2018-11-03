package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Controller extends AbstractActor {

    private Reactor reactor;

    public Controller(Reactor reactor) {
        setAnimation(new Animation("sprites/switch.png", 16, 16));
        this.reactor = reactor;
    }

    public void toggle() {
        if(reactor.isRunning()) {
            reactor.turnOff();
        } else {
            reactor.turnOn();
        }
    }
}
