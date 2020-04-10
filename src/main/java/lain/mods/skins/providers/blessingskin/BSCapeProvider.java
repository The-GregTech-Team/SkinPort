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

public class BSCapeProvider implements ISkinProvider {
    private Function<ByteBuffer, ByteBuffer> _filter;
    private String _host;

    public BSCapeProvider() {
    }

    public ISkin getSkin(IPlayerProfile profile) {
        SkinData skin = new SkinData();
        if (this._filter != null) {
            skin.setSkinFilter(this._filter);
        }

        SharedPool.execute(() -> {
            Shared.downloadSkin(String.format("%s/cape/%s.png", this._host, profile.getPlayerName()), Runnable::run).thenApply(Optional::get).thenAccept((data) -> {
                if (SkinData.validateData(data)) {
                    skin.put(data, "cape");
                }
            });
        });
        return skin;
    }

    public BSCapeProvider setHost(String host) {
        this._host = host;
        return this;
    }

    public BSCapeProvider withFilter(Function<ByteBuffer, ByteBuffer> filter) {
        this._filter = filter;
        return this;
    }
}
