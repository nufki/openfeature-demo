package com.openfeature_demo;

import java.util.Optional;

import dev.openfeature.sdk.MutableContext;

public class RaiContext extends MutableContext{

    public RaiContext(String contractNr){
        String contract = Optional.ofNullable(contractNr).orElse("");
        super.add("contract", contract);
    }

    public RaiContext(Object token){
        // super.add("contract", token.getContractNr);
        // super.add("bu", token.getBu);
        // ...
    }

}
