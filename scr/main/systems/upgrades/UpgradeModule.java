package main.world.systems.upgrades;

import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import main.type.upgrade.Upgrade;
import main.type.upgrade.Upgradeable;
import mindustry.world.modules.BlockModule;

public class UpgradeModule extends BlockModule {

    public Seq<UpgradeState> states = new Seq<>();

    public UpgradeModule(){

    }

    public UpgradeState getState(Upgrade upgrade){
        UpgradeState state = states.find(s -> s.upgrade == upgrade);
        if(state == null) {
            state = new UpgradeState(upgrade);
            states.add(state);
        }
        return state;
    }

    public void update(Upgradeable u){
        states.each(s -> {
            if(!s.installing && s.installed) u.applyDeltas(s);
        });
    }

    @Override
    public void write(Writes write){
        write.i(states.size);
        states.each(s -> s.write(write));
    }

    @Override
    public void read(Reads read, boolean legacy) {
        int size = read.i();
        for (int i = 0; i < size; i++) {
            states.add(new UpgradeState().read(read));
        }
    }

    public float getProgress(UpgradeState.ProgressType type){
        float total = 0;
        switch (type){
            case PER_ITEM: {
                int each = 0;
                for(UpgradeState state : states){
                    if(state.progress < 1) {
                        total += state.progress;
                        each++;
                    }
                }
                total /= each;
                break;
            }
            case TOTAL: {
                for(UpgradeState state : states){
                    total += state.progress;
                }
                total /= states.size;
            }
        }
        return total;
    }

    public void startUpgrade(UpgradeEntry entry) {
        UpgradeState current = states.find(state -> state.upgrade == entry.upgrade);
        //if no state is found create a new one
        if(current == null){
            states.add(current = new UpgradeState(entry.upgrade, entry.costs[0]));
            if(UpgradeHandler.get().instantUpgrades){
                current.level = 0;
                current.installed = true;
                current.installing = false;
            }
            return;
        }
        //Don't create a new state if it's maxed
        if(current.level == (entry.stacks() - 1)) return;
        //start on the next stack
        if(UpgradeHandler.get().instantUpgrades){
            current.level++;
            current.installing = false;
            current.installed = true;
        }else {
            current.installing = true;
            current.progress = 0;
            current.cost = entry.costs[current.level];
        };
    }
}
