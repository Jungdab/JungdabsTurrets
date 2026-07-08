package me.jungdab.jdturrets.platform;

import me.jungdab.jdturrets.Constants;

import java.util.ServiceLoader;

public class Services {

    public static IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static final INetworkHelper NETWORK = load(INetworkHelper.class);

    public static final IRegistry REGISTRY = load(IRegistry.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}