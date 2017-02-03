package ru.ilia.rest.service;

/**
 * Загружает сервисы по пути /ws/
 **/
import java.util.Set;
import java.util.HashSet;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/ws/")
public class RestfulLoader extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(RootRS.class);
        set.add(MonitorsRS.class);
        set.add(CamerasRS.class);
        return set;
    }
}
