package com.openfeature_demo.openfeature;

import dev.openfeature.sdk.MutableContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.stream.Collectors;

public class RaiContext extends MutableContext {

    public RaiContext(String contractNr) {
        String contract = Optional.ofNullable(contractNr).orElse("");
        super.add("contract", contract);
    }

    public RaiContext(Object token) {
        String role = (token instanceof UserDetails)
                ? ((UserDetails) token).getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r.replaceFirst("^ROLE_", ""))
                .collect(Collectors.joining(","))
                : null;

        super.add("role", role);
        // more attributes
        // super.add("contract", token.getContractNr);
        // super.add("bu", token.getBu);
        // ...
    }

}
