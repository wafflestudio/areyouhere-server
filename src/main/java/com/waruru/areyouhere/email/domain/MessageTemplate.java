package com.waruru.areyouhere.email.domain;

public enum MessageTemplate {
    PASSWORD_RESET("You can reset password by this link: %s"),
    SIGN_UP("You can verify your email by this link: %s");

    private final String template;

    MessageTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
