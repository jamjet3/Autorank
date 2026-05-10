package me.armar.plugins.autorank.statsmanager.query.parameter;

import io.reactivex.annotations.NonNull;
import java.lang.reflect.InvocationTargetException;
import me.armar.plugins.autorank.statsmanager.query.parameter.implementation.BlockTypeParameter;
import me.armar.plugins.autorank.statsmanager.query.parameter.implementation.FoodTypeParameter;
import me.armar.plugins.autorank.statsmanager.query.parameter.implementation.MobTypeParameter;
import me.armar.plugins.autorank.statsmanager.query.parameter.implementation.MovementTypeParameter;
import me.armar.plugins.autorank.statsmanager.query.parameter.implementation.WorldTypeParameter;
import org.apache.commons.lang.Validate;

public enum ParameterType {
    WORLD(WorldTypeParameter.class),
    MOB_TYPE(MobTypeParameter.class),
    BLOCK_TYPE(BlockTypeParameter.class),
    MOVEMENT_TYPE(MovementTypeParameter.class),
    FOOD_TYPE(FoodTypeParameter.class);

    private final Class<? extends StatisticParameter> matchingParameter;

    ParameterType(Class<? extends StatisticParameter> parameter) {
        this.matchingParameter = parameter;
    }

    public static ParameterType getParameterType(@NonNull String key) {
        Validate.notNull(key);

        for(ParameterType type : values()) {
            try {
                if (type.getMatchingParameter().getDeclaredConstructor(String.class).newInstance("").getKey().equalsIgnoreCase(key)) {
                    return type;
                }
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException var6) {
                var6.printStackTrace();
            }
        }

        return null;
    }

    @NonNull
    public Class<? extends StatisticParameter> getMatchingParameter() {
        return this.matchingParameter;
    }

    public String getKey() {
        try {
            return this.getMatchingParameter().getDeclaredConstructor(String.class).newInstance("").getKey();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException var2) {
            var2.printStackTrace();
            return null;
        }
    }
}
