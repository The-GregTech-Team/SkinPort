package lain.mods.skins.providers.blessingskin;

import lain.lib.SharedPool;
import lain.mods.skins.api.interfaces.IPlayerProfile;
import lain.mods.skins.api.interfaces.ISkin;
import lain.mods.skins.api.interfaces.ISkinProvider;
import lain.mods.skins.impl.Shared;
import lain.mods.skins.impl.SkinData;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Function;

public class BSSkinProvider implements ISkinProvider {
    private Function<ByteBuffer, ByteBuffer> _filter;
    private String _host;

    public BSSkinProvider() {
    }

    public ISkin getSkin(IPlayerProfile profile) {
        SkinData skin = new SkinData();
        if (this._filter != null) {
            skin.setSkinFilter(this._filter);
        }

        if (Shared.isOfflinePlayer(profile.getPlayerID(), profile.getPlayerName()))
            SharedPool.execute(() -> {
                Shared.downloadSkin(String.format("%s/skin/%s.png", this._host, profile.getPlayerName()), Runnable::run).thenApply(Optional::get).thenAccept((data) -> {
                    try {
                        if (SkinData.validateData(data)) {
                            skin.put(data, SkinData.judgeSkinType(data));
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }

                });
            });
        return skin;
    }

    public BSSkinProvider setHost(String host) {
        this._host = host;
        return this;
    }

    public BSSkinProvider withFilter(Function<ByteBuffer, ByteBuffer> filter) {
        this._filter = filter;
        return this;
    }
}
