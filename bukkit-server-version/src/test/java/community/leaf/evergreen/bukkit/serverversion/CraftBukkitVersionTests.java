package community.leaf.evergreen.bukkit.serverversion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CraftBukkitVersionTests
{
    @Test
    public void testParse()
    {
        assertThat(CraftBukkitVersion.parseCraftBukkitVersion("org.bukkit.craftbukkit.v1_18_R1.CraftServer"))
            .hasValue(new CraftBukkitVersion(1, 18, 1));
        
        assertThat(CraftBukkitVersion.parseCraftBukkitVersion("org.bukkit.craftbukkit.v1_8_R3.CraftServer"))
            .hasValue(new CraftBukkitVersion(1, 8, 3));
    }
    
    @Test
    public void testToString()
    {
        assertThat(new CraftBukkitVersion(1, 18, 1).toString()).isEqualTo("v1_18_R1");
        assertThat(new CraftBukkitVersion(1, 8, 3).toString()).isEqualTo("v1_8_R3");
    }
    
    @Test
    public void testPackages()
    {
        CraftBukkitVersion v1_17_1 = new CraftBukkitVersion(1, 17, 1);
        
        assertThat(v1_17_1.craftBukkitPackage()).isEqualTo("org.bukkit.craftbukkit.v1_17_R1");
        assertThat(v1_17_1.craftBukkitClass("Demo")).isEqualTo("org.bukkit.craftbukkit.v1_17_R1.Demo");
        assertThat(v1_17_1.minecraftPackage()).isEqualTo("net.minecraft");
        assertThat(v1_17_1.minecraftClass("Demo")).isEqualTo("net.minecraft.Demo");
        
        CraftBukkitVersion v1_16_3 = new CraftBukkitVersion(1, 16, 3);
        
        assertThat(v1_16_3.craftBukkitPackage()).isEqualTo("org.bukkit.craftbukkit.v1_16_R3");
        assertThat(v1_16_3.craftBukkitClass("Demo")).isEqualTo("org.bukkit.craftbukkit.v1_16_R3.Demo");
        assertThat(v1_16_3.minecraftPackage()).isEqualTo("net.minecraft.server.v1_16_R3");
        assertThat(v1_16_3.minecraftClass("Demo")).isEqualTo("net.minecraft.server.v1_16_R3.Demo");
    }
}
