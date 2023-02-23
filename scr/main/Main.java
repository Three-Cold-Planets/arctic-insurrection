package main;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Cell;
import arc.struct.ObjectFloatMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Structs;
import arc.util.Time;
import main.content.Palf;
import main.game.ScriptedSectorHandler;
import main.graphics.FrostShaders;
import main.mods.Compatibility;
import main.type.HollusUnitType.LoadableEngine;
import main.type.upgrade.Upgrade;
import main.ui.FrostUI;
import main.ui.ModTex;
import main.ui.overlay.ScanningOverlay;
import main.ui.overlay.SelectOverlay;
import main.util.UIUtils;
import main.world.meta.Family;
import main.world.meta.LoreNote;
import main.world.systems.heat.TileHeatControl;
import main.world.systems.light.LightBeams;
import main.world.systems.research.ResearchHandler;
import main.world.systems.upgrades.UpgradeHandler;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import mindustry.gen.Icon;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.io.SaveVersion;
import mindustry.mod.Mod;
import mindustry.mod.Mods;
import mindustry.ui.dialogs.BaseDialog;
import rhino.ImporterTopLevel;
import rhino.NativeJavaPackage;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static mindustry.Vars.ui;

public class Main extends Mod{

    public static NativeJavaPackage p = null;

    public static final String NAME = "arctic-insurection";
    public static Mods.LoadedMod MOD;
    public static final float VERSION = 136.1f;
    public static String VERSION_NAME = "", LAST_VERSION_NAME = "";

    public Main(){

        Color.cyan.set(Palf.pulseChargeEnd);
        Color.sky.set(Palf.pulseChargeStart);

        Events.on(FileTreeInitEvent.class, e -> {
            MOD = Vars.mods.getMod(NAME);
        });

        Events.run(ContentInitEvent.class, this::loadSplash);

        Events.run(WinEvent.class, this::loadSplash);

        //Most of theese are singletons for the sake of being able to port these over to the Arctic-Insurrection mod more easly.
        SaveVersion.addCustomChunk("upgrade-handler", UpgradeHandler.get());
        Events.run(EventType.ClientLoadEvent.class, () -> {
            UpgradeHandler.upgrades.each(Upgrade::load);
        });

        SaveVersion.addCustomChunk("research-handler", ResearchHandler.get());
        SaveVersion.addCustomChunk("tile-heat-control", TileHeatControl.get());
        SaveVersion.addCustomChunk("light-beams", LightBeams.get());

        Events.on(StateChangeEvent.class, e -> {
            if(e.from == GameState.State.playing && e.to == GameState.State.menu) LightBeams.get().lights.clear();
        });

        Events.run(Trigger.update, () -> {
            if(!Vars.state.isPlaying()) return;
            LightBeams.get().updateBeams();
        });

        Events.run(Trigger.draw, () -> {
            Draw.draw(Layer.overlayUI, selection::drawSelect);
            Draw.draw(Layer.overlayUI, scan::draw);
            Draw.draw(Layer.buildBeam, scan::drawScan);
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
