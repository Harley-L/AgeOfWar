package com.ageofwar;

import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.component.Component;

public class VelocityComponent extends Component {
    private ProjectileComponent physics;

    @Override
    public void onUpdate(double tpf) {}

    public void begin_collision() { physics.pause(); }
    public void end_collision() { physics.resume(); }
}
