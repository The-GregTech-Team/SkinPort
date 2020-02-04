package lain.mods.skins.providers.customskinloader;

import lain.lib.SharedPool;
import lain.mods.skins.api.interfaces.IPlayerProfile;
import lain.mods.skins.api.interfaces.ISkin;
import lain.mods.skins.api.interfaces.ISkinProvider;
import lain.mods.skins.impl.Shared;
import lain.mods.skins.impl.SkinData;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public class CSLSkinProvider implements ISkinProvider {
    public CSLSkinSiteProfile[] loadlist;

    public File configFile = Minecraft.getMinecraft().mcDataDir.toPath().resolve("CustomSkinLoader").resolve("CustomSkinLoader.json").toAbsolutePath().toFile();

    public CSLSkinProvider() {
        if (configFile.exists()) {
            CSLConfig config = null;
            try {
                config = CSLConfig.GSON.fromJson(new FileReader(configFile), CSLConfig.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (config != null && config.loadlist != null) {
                loadlist = config.loadlist;
            }
        }
    }

    @Override
    public ISkin getSkin(IPlayerProfile profile) {
        SkinData skin = new SkinData();
        if (loadlist != null && Shared.isOfflinePlayer(profile.getPlayerID(), profile.getPlayerName())) {
            Arrays.stream(loadlist).filter(site -> site.type.equals("CustomSkinAPI")).forEach(site -> {
                SharedPool.execute(() -> {
                    Shared.downloadSkin(site.root + profile.getPlayerName() + ".json", Runnable::run)
                            .thenApply(bytes -> new String(bytes.get(), StandardCharsets.UTF_8))
                            .thenApply(str -> CSLConfig.GSON.fromJson(str, CSLJsonAPIProfile.class))
                            .thenAccept(apiProfile -> {
                                String skinStr = apiProfile.skins != null && apiProfile.skins.containsKey("default") ? apiProfile.skins.get("default") : apiProfile.skin;
                                Shared.downloadSkin(site.root + "textures/" + skinStr, Runnable::run).thenApply(Optional::get).thenAccept(data -> {
                                    try {
                                        if (SkinData.validateData(data)) {
                                            skin.put(data, SkinData.judgeSkinType(data));
                                            System.out.println("Data valid");
                                        }
                                    } catch (Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                            });
                });
            });
        }
        return skin;
    }
}
