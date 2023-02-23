package common.world.blocks.defense;

import arc.audio.Sound;
import arc.graphics.g2d.Draw;
import common.world.BaseBlock;
import common.world.BaseBuilding;
import common.world.UpgradesType;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.world.draw.DrawBlock;

public class UpgradeableMine extends BaseBlock {
    public final int timerDamage = timers++;

    public float cooldown = 80f;
    public float tileDamage = 5f;
    public float teamAlpha = 0.3f;
    public boolean friendlyFire = true;
    public Sound shootSound = Sounds.spark;
    public float soundMinPitch = 0.8f, soundMaxPitch = 1.1f;
    public DrawBlock drawer;

    public UpgradeableMine(String name) {
        super(name);
        destructible = true;
        solid = false;
        targetable = false;
        hasShadow = false;
    }

    @Override
    public void load(){
        super.load();
        drawer.load(this);
    }

    public class UpgradeableMineBuild extends BaseBuilding {

        @Override
        public void drawTeam(){
            //no
        }

        @Override
        public void draw(){
            drawer.draw(this);
            Draw.color(team.color, teamAlpha);
            Draw.rect(teamRegion, x, y);
            Draw.color();
        }

        @Override
        public void drawCracks(){
            //no
        }

        @Override
        public void unitOn(Unit unit){
            if(enabled && (unit.team != team || friendlyFire) && timer(timerDamage, cooldown)){
                triggered();
                damage(tileDamage);
            }
        }

        public void triggered(){

        };

        @Override
        public UpgradesType type() {
            return (UpgradesType) block;
        }
    }
}
