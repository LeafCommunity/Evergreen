/*
 * Copyright © 2022, RezzedUp <https://github.com/LeafCommunity/Evergreen>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.evergreen.bukkit.serverversion;

import org.bukkit.Bukkit;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CraftBukkitVersion extends MinecraftVersion
{
    private static final Pattern VERSION_PATTERN =
        Pattern.compile("v(?<release>\\d+)_(?<major>\\d+)_R(?<revision>\\d+)");
    
    private static final Pattern PACKAGE_VERSION_PATTERN =
        Pattern.compile("org\\.bukkit\\.craftbukkit\\.(?<version>" + VERSION_PATTERN + ")\\.");
    
    private static @NullOr CraftBukkitVersion SERVER;
    
    public static CraftBukkitVersion server()
    {
        if (SERVER == null)
        {
            String serverClassName = Bukkit.getServer().getClass().getCanonicalName();
            Matcher matcher = PACKAGE_VERSION_PATTERN.matcher(serverClassName);
            
            if (!matcher.find())
            {
                throw new IllegalStateException("Cannot resolve version from package: " + serverClassName);
            }
            
            SERVER = parseCraftBukkitVersion(matcher.group("version")).orElseThrow(() ->
                new IllegalStateException("Cannot resolve version: " + matcher.group("version"))
            );
        }
        
        return SERVER;
    }
    
    public static Optional<CraftBukkitVersion> parseCraftBukkitVersion(String text)
    {
        Objects.requireNonNull(text, "text");
        
        Matcher matcher = VERSION_PATTERN.matcher(text);
        
        return (matcher.find())
            ? Optional.of(new CraftBukkitVersion(
                intOrZero(matcher.group("release")),
                intOrZero(matcher.group("major")),
                intOrZero(matcher.group("revision"))
            ))
            : Optional.empty();
    }
    
    private final String packageVersion;
    private final String craftBukkitPackage;
    private final String minecraftPackage;
    
    public CraftBukkitVersion(int major, int minor, int patch)
    {
        super(major, minor, patch);
        this.packageVersion = "v" + major + "_" + minor + "_R" + patch;
        this.craftBukkitPackage = "org.bukkit.craftbukkit." + packageVersion;
        this.minecraftPackage = (atLeast(1, 17)) ? "net.minecraft" : "net.minecraft.server." + packageVersion;
    }
    
    public String craftBukkitPackage() { return craftBukkitPackage; }
    
    public String craftBukkitClass(String className) { return craftBukkitPackage + "." + className; }
    
    public String minecraftPackage() { return minecraftPackage; }
    
    public String minecraftClass(String className) { return minecraftPackage + "." + className; }
    
    @Override
    public String toString()
    {
        return packageVersion;
    }
}
