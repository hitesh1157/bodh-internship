package common.settings;

import com.google.inject.AbstractModule;

public class S3Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(S3.class).asEagerSingleton();
    }

}