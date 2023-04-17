package org.example.configuration;

import java.util.function.Supplier;

public class Configurations {
    public enum ConfigurationType {
        W3C(ConfigurationW3C::new),
        W3C404(ConfigurationW3C404::new),
        NCSA(ConfigurationNCSA::new);

        private final Supplier<IConfiguration> instantiator;

        public IConfiguration getInstance() {
            return instantiator.get();
        }

        ConfigurationType(Supplier<IConfiguration> instantiator) {
            this.instantiator = instantiator;
        }
    }

    public static IConfiguration getConfiguration( ConfigurationType config ) {
        return config.getInstance();
    }
}
