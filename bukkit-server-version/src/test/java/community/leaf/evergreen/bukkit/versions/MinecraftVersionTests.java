/*
 * Copyright © 2022, RezzedUp <https://github.com/LeafCommunity/Evergreen>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.evergreen.bukkit.versions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MinecraftVersionTests
{
    @Test
    public void testParse()
    {
        assertThat(MinecraftVersion.parseMinecraftVersion("1.18.1-R0.1-SNAPSHOT"))
            .hasValue(new MinecraftVersion(1, 18, 1));
        
        assertThat(MinecraftVersion.parseMinecraftVersion("1.16.5-R0.1-SNAPSHOT"))
            .hasValue(new MinecraftVersion(1, 16, 5));
        
        assertThat(MinecraftVersion.parseMinecraftVersion("1.8.9"))
            .hasValue(new MinecraftVersion(1, 8, 9));
        
        assertThat(MinecraftVersion.parseMinecraftVersion("1.7"))
            .hasValue(new MinecraftVersion(1, 7, 0));
        
        assertThat(MinecraftVersion.parseMinecraftVersion("2"))
            .hasValue(new MinecraftVersion(2, 0, 0));
    }
    
    @Test
    public void testComparison()
    {
        assertThat(new MinecraftVersion(1, 17, 1))
            .isEqualByComparingTo(new MinecraftVersion(1, 17, 1))
            .isGreaterThan(new MinecraftVersion(1, 17, 0))
            .isGreaterThan(new MinecraftVersion(1, 16, 0))
            .isLessThan(new MinecraftVersion(1, 17, 2))
            .isLessThan(new MinecraftVersion(1, 18, 1))
            .isLessThan(new MinecraftVersion(2, 0, 0))
            .matches(ver -> ver.lessThan(3))
            .matches(ver -> ver.greaterThan(1, 2, 3))
            .matches(ver -> ver.atMost(1, 18))
            .matches(ver -> ver.atMost(1, 17))
            .matches(ver -> ver.atLeast(1, 17))
            .matches(ver -> ver.atLeast(1, 6));
    }
    
    @Test
    public void testToString()
    {
        assertThat(new MinecraftVersion(1,  7, 3).toString()).isEqualTo("1.7.3");
        assertThat(new MinecraftVersion(2,  0, 0).toString()).isEqualTo("2.0.0");
    }
    
    @Test
    public void testNegative()
    {
        assertThatThrownBy(() -> new MinecraftVersion(-1, 5, 2))
            .isInstanceOf(IllegalArgumentException.class);
        
        assertThatThrownBy(() -> new MinecraftVersion(0, -5, 4))
            .isInstanceOf(IllegalArgumentException.class);
        
        assertThatThrownBy(() -> new MinecraftVersion(0, 0, Integer.MIN_VALUE))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
