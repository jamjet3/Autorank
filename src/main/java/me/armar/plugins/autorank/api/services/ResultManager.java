package me.armar.plugins.autorank.api.services;

import java.util.List;
import java.util.Optional;
import me.armar.plugins.autorank.pathbuilder.result.AbstractResult;

public interface ResultManager {
    boolean registerResult(String var1, Class<? extends AbstractResult> var2);

    boolean unRegisterResult(String var1);

    List<Class<? extends AbstractResult>> getRegisteredResults();

    Optional<Class<? extends AbstractResult>> getResult(String var1);
}