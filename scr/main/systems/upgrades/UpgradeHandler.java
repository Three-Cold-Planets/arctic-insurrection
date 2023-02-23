package main.world.systems.upgrades;

import arc.Events;
import arc.struct.*;
import arc.util.io.Reads;
import arc.util.io.Writes;
import main.type.upgrade.Upgrade;
import mindustry.game.EventType;
import mindustry.io.SaveFileReader.CustomChunk;
import mindustry.io.SaveVersion;

import java.io.*;

public class UpgradeHandler implements CustomChunk{

    public static Seq<Upgrade> upgrades = new Seq<Upgrade>();

    public static UpgradeHandler instance;

    public static UpgradeHandler get(){
        if(instance == null) return instance = new UpgradeHandler();
        return instance;
    }

    //Makes upgrades instant
    public boolean instantUpgrades = true;

    @Override
    public void write(DataOutput stream) throws IOException {
        Writes write = new Writes(stream);
        write.bool(instantUpgrades);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        Reads reads = new Reads(stream);
        instantUpgrades = reads.bool();
    }
}
