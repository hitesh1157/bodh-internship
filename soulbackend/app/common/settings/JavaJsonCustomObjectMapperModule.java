package common.settings;

import com.google.inject.AbstractModule;

public class JavaJsonCustomObjectMapperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(JavaJsonCustomObjectMapper.class).asEagerSingleton();
    }

}