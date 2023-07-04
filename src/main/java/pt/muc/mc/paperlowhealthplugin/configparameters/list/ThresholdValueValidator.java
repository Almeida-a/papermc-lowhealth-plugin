package pt.muc.mc.paperlowhealthplugin.configparameters.list;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Optional;
import java.util.function.Predicate;

public class ThresholdValueValidator implements Predicate<String> {

    @Override
    public boolean test(String confValue) {

        int intValue = Optional.ofNullable(confValue)
                .map(NumberUtils::toInt)
                .orElse(-1);

        return intValue >= 0 && intValue <= 100;
    }
}
