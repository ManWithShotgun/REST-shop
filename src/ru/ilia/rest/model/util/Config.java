package ru.ilia.rest.model.util;

import java.util.ResourceBundle;

/**
 * Created by ILIA on 05.02.2017.
 */
public class Config {
    private static final String APP_PROPS="app";
    public static String PATH_IMG;
    static {
        ResourceBundle props=ResourceBundle.getBundle(Config.APP_PROPS);
        Config.PATH_IMG=props.getString("relativePath.toImage");
    }
}
