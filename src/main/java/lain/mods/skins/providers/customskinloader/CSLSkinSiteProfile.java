package lain.mods.skins.providers.customskinloader;

public class CSLSkinSiteProfile {
    public String name;
    public String type;
    public Boolean local = null;
    public String root;
    public String skin;
    public String cape;
    public String model;
    public String userAgent;

    public static CSLSkinSiteProfile createMojangAPI(String name) {
        CSLSkinSiteProfile ssp = new CSLSkinSiteProfile();
        ssp.name = name;
        ssp.type = "MojangAPI";
        return ssp;
    }

    public static CSLSkinSiteProfile createCustomSkinAPI(String name, String root) {
        return createCustomSkinAPI(name, false, root);
    }

    public static CSLSkinSiteProfile createCustomSkinAPI(String name, boolean local, String root) {
        CSLSkinSiteProfile ssp = new CSLSkinSiteProfile();
        ssp.name = name;
        ssp.type = "CustomSkinAPI";
        if (local)
            ssp.local = true;
        ssp.root = root;
        return ssp;
    }

    public static CSLSkinSiteProfile createUniSkinAPI(String name, String root) {
        return createUniSkinAPI(name, false, root);
    }

    public static CSLSkinSiteProfile createUniSkinAPI(String name, boolean local, String root) {
        CSLSkinSiteProfile ssp = new CSLSkinSiteProfile();
        ssp.name = name;
        ssp.type = "UniSkinAPI";
        if (local)
            ssp.local = true;
        ssp.root = root;
        return ssp;
    }

    public static CSLSkinSiteProfile createLegacy(String name, String skin, String cape) {
        return createLegacy(name, false, skin, cape);
    }

    public static CSLSkinSiteProfile createLegacy(String name, boolean local, String skin, String cape) {
        CSLSkinSiteProfile ssp = new CSLSkinSiteProfile();
        ssp.name = name;
        ssp.type = "Legacy";
        if (local)
            ssp.local = true;
        ssp.skin = skin;
        ssp.cape = cape;
        return ssp;
    }
}
