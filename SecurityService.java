package com.acme.intelligence.service;

import com.acme.intelligence.support.Role;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class SecurityService {

    private final Map<Role, Set<String>> permissions = Map.of(
            Role.ADMIN, Set.of(
                    "investigate",
                    "approve",
                    "create_jira_ticket",
                    "send_slack_alert"
            ),
            Role.ANALYST, Set.of("investigate"),
            Role.SUPPORT_ENGINEER, Set.of(
                    "investigate",
                    "create_jira_ticket",
                    "send_slack_alert"
            ),
            Role.VIEWER, Set.of()
    );

    public boolean hasPermission(Role role, String permission) {
        return permissions.getOrDefault(role, Set.of()).contains(permission);
    }
}
