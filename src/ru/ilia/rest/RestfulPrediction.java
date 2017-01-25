package ru.ilia.rest;

/**
 * Created by ILIA on 24.01.2017.
 */
import java.util.Set;
import java.util.HashSet;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/ws/")
public class RestfulPrediction extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(PredictionsRS.class);
        set.add(MonitorsRS.class);
        return set;
    }
}
