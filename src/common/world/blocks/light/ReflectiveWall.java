package common.world.blocks.light;

import arc.math.geom.Rect;
import arc.struct.Seq;
import main.Frostscape;
import main.math.Mathh;
import common.world.BaseBlock;
import common.world.BaseBuilding;
import common.world.UpgradesType;
import common.world.systems.light.LightBeams;
import common.world.systems.light.WorldShape;
import common.world.systems.light.Lightc;
import common.world.systems.light.shape.WorldRect;

import static mindustry.Vars.tilesize;

public class ReflectiveWall extends BaseBlock {

    public static WorldShape[] tmp = new WorldShape[1];
    public ReflectiveWall(String name) {
        super(name);
    }

    public class ReflectiveWallBuild extends BaseBuilding implements Lightc {

        public WorldRect hitbox = new WorldRect();
        @Override
        public void created() {
            LightBeams.().lights.handle(this);
        }

        @Override
        public boolean exists() {
            return added;
        }

        @Override
        public boolean collides() {
            return true;
        }

        @Override
        public void hitbox(Rect out) {
            super.hitbox(out);
        }

        @Override
        public UpgradesType type() {
            return (UpgradesType) block;
        }

        @Override
        public float reflectivity(int shape, int side) {
            return 1;
        }

        @Override
        public void hitboxes(Seq<WorldShape> sequence){
            sequence.add(hitbox.set(x - size * tilesize/2, y - size * tilesize/2, size * tilesize, size * tilesize));
        }
        @Override
        public LightBeams.CollisionData collision(float x, float y, float rotation, int shape, int side, LightBeams.ColorData color, LightBeams.CollisionData collision) {
            float newRot = rotation;

            boolean flipX = false, flipY = true;

            if((side/2 % 2) == 0) {
                flipX = true;
                flipY = false;
            };

            if(flipX) newRot = Mathh.rotReflectionX(rotation);
            if(flipY) newRot = Mathh.rotReflectionY(rotation);

            collision.rotAfter = newRot;

            return collision;
        }
    }
}
