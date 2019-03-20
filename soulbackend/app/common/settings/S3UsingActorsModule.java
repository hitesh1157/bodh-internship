package common.settings;

import com.google.inject.AbstractModule;

public class S3UsingActorsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(S3UsingActors.class).asEagerSingleton();
    }
}
