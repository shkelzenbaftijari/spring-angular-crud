// ════════════════════════════════════════════════════════
// · CONSTANTS · Application-wide constant definitions
// ════════════════════════════════════════════════════════
package com.springcrud.config;

public final class Constants {

    // Login allows: simple username (john_doe) OR email format (john@doe.com)
    public static final String LOGIN_REGEX =
        "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM_ACCOUNT = "system";

    private Constants() {}
}
