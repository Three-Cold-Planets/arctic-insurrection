package common.world;

import arc.struct.Seq;
import main.type.upgrade.Upgrade;
import main.world.blocks.drawers.UpgradeDrawer;

public interface UpgradesBlock extends UpgradesType {
    Seq<Upgrade> upgrades();
    Seq<UpgradeDrawer> drawers();
}
