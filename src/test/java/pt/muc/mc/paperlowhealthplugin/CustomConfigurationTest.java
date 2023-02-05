package pt.muc.mc.paperlowhealthplugin;

import org.apache.commons.text.CaseUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
class CustomConfigurationTest {

    @org.junit.jupiter.api.Test
    void nameToLowerCamelCase() {
        assertEquals(
                CaseUtils.toCamelCase("THIS_STRING_INTO_LOWER_CAMEL_CASE", false, '_'),
                "thisStringIntoLowerCamelCase"
        );
    }

    @org.junit.jupiter.api.Test
    void loadConfiguration() {
        // TODO: 05/02/23 Implement test
        fail("Test not implemented");
    }
}