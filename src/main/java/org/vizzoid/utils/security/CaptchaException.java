package org.vizzoid.utils.security;

import java.io.Serial;

/**
 * For exceptions such as security exceptions and other events which cause a Captcha to be failed to be created or used
 */
public class CaptchaException extends Exception {
    @Serial
    private static final long serialVersionUID = 915757108625693006L;

    public CaptchaException(String message) {
        super(message);
    }

    public CaptchaException(Throwable cause) {
        super(cause);
    }
}
