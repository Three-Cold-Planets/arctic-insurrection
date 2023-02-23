package main;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import main.world.systems.heat.*;
import main.world.systems.light.*;
import main.world.systems.research.*;
import main.world.systems.upgrades.*;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.graphics.Layer;
import mindustry.io.SaveVersion;
import mindustry.mod.Mod;
import mindustry.mod.Mods;
import rhino.NativeJavaPackage;

import static mindustry.Vars.ui;

public class Main extends Mod {

    public static NativeJavaPackage p = null;

    public static final String NAME = "arctic-insurection";
    public static Mods.LoadedMod MOD;
    public static final float VERSION = 136.1f;
    public static String VERSION_NAME = "", LAST_VERSION_NAME = "";

    public Main(){

        Events.on(EventType.FileTreeInitEvent.class, e -> {
            MOD = Vars.mods.getMod(NAME);
        });

        //Most of theese are singletons for the sake of being able to port these over to the Arctic-Insurrection mod more easly.
        SaveVersion.addCustomChunk("upgrade-handler", UpgradeHandler.get());
        Events.run(EventType.ClientLoadEvent.class, () -> {
            UpgradeHandler.upgrades.each(Upgrade::load);
        });

        SaveVersion.addCustomChunk("research-handler", ResearchHandler.get());
        SaveVersion.addCustomChunk("tile-heat-control", TileHeatControl.get());
        SaveVersion.addCustomChunk("light-beams", LightBeams.get());

        Events.on(EventType.StateChangeEvent.class, e -> {
            if(e.from == GameState.State.playing && e.to == GameState.State.menu) LightBeams.get().lights.clear();
        });

        Events.run(EventType.Trigger.update, () -> {
            if(!Vars.state.isPlaying()) return;
            LightBeams.get().updateBeams();
        });

        Events.run(EventType.Trigger.draw, () -> {
            Draw.draw(Layer.light + 1, LightBeams.get()::draw);
        });
    }

    void loadSettingsloadSettings(){
        ui.settings.addCategory(Core.bundle.get("settings.frostscape-title"), NAME + "-hunter", t -> {
            t.sliderPref(Core.bundle.get("frostscape-parallax"), 100, 1, 100, 1, s -> s + "%");
            t.sliderPref(Core.bundle.get("frostscape-wind-visual-force"), 100, 0, 800, 1, s -> s + "%");
        });
    }
}
