package common.world.blocks.environment;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Time;
import main.content.Fxf;
import mindustry.content.Blocks;
import mindustry.entities.Effect;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
public class CrackedBlock extends Floor {

    public Effect glowEffect = Fxf.glowEffect;
    public float blinkTimeRange = 35, maxBlinkTime = 105;
    public Floor cracked = (Floor) Blocks.slag;
    public TextureRegion[] topVariantRegions;

    public CrackedBlock(String name) {
        super(name);
    }

    public TextureRegion[] topVariantRegions() {
        return topVariantRegions == null ? (topVariantRegions = new TextureRegion[]{fullIcon}) : variantRegions;
    }

    @Override
    public void load() {
        super.load();
        if (variants != 0) {
            topVariantRegions = new TextureRegion[variants];

            for(int i = 0; i < variants; ++i) {
                topVariantRegions[i] = Core.atlas.find(name + "-top" + (i + 1));
            }
        }
    }

    public float crackTime = 100;

    @Override
    public boolean updateRender(Tile tile) {
        return true;
    }

    @Override
    public void renderUpdate(UpdateRenderState tile) {
        super.renderUpdate(tile);

        int index = Mathf.randomSeed(tile.tile.pos(), 0, Math.max(0, variantRegions.length - 1));

        float lava = maxBlinkTime - lava(tile.tile.x, tile.tile.y) * blinkTimeRange;

        tile.data += Time.delta;
        if(tile.data >= maxBlinkTime + lava) {
            tile.data = lava;
            glowEffect.lifetime = maxBlinkTime;
            glowEffect.create(tile.tile.worldx(), tile.tile.worldy(), 0, Color.white, topVariantRegions[index]);
        }
    }

    //Think of it like a shader for effect delay
    public float lava(int x, int y){
        return Mathf.cos(x + y + Time.time/maxBlinkTime);
    }
}
