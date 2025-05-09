package ru.imagebook.server.service.auth;

public class AuthConfig {
    private final int connectionLifetimeMS;
    private final int maxClientResponseTimeMS;
    private final int sessionLifetimeMS;

    public AuthConfig(int connectionLifetimeSec, int maxClientResponseTimeSec, int sessionLifetimeMin) {
        this.connectionLifetimeMS = connectionLifetimeSec * 1000;
        this.maxClientResponseTimeMS = maxClientResponseTimeSec * 1000;
        this.sessionLifetimeMS = sessionLifetimeMin * 60 * 1000;
    }

    public int getConnectionLifetimeMS() {
        return connectionLifetimeMS;
    }

    public int getMaxClientResponseTimeMS() {
        return maxClientResponseTimeMS;
    }

    public int getSessionLifetimeMS() {
        return sessionLifetimeMS;
    }
}
