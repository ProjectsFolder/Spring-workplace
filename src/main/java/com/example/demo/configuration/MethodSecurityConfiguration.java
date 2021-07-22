package com.example.demo.configuration;

import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.util.ArrayList;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public RoleHierarchy roleHierarchy() {
        var roles = roleRepository.findAll();
        var arrayHierarchy = new ArrayList<String>(roles.size());
        for (var role: roles) {
            var parent = role.getParent();
            if (null != parent) {
                arrayHierarchy.add(parent.getName() + " > " + role.getName());
            }
        }
        var hierarchy = String.join("\n", arrayHierarchy);

        var roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(hierarchy);

        return roleHierarchy;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        var handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy());

        return handler;
    }

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        var voters = new ArrayList<AccessDecisionVoter<?>>();
        voters.add(new RoleHierarchyVoter(roleHierarchy()));

        return new AffirmativeBased(voters);
    }

}
