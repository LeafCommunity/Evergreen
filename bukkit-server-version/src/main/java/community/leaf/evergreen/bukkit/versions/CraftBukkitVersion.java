/*
 * Copyright © 2022-2023, RezzedUp <https://github.com/LeafCommunity/Evergreen>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.evergreen.bukkit.versions;

import org.bukkit.Bukkit;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the internal package version of a Bukkit-based server.
 */
@SuppressWarnings("unused")
public class CraftBukkitVersion extends MinecraftVersion
{
    private static final Pattern VERSION_PATTERN =
        Pattern.compile("v(?<release>\\d+)_(?<major>\\d+)_R(?<revision>\\d+)");
    
    private static final Pattern PACKAGE_VERSION_PATTERN =
        Pattern.compile("org\\.bukkit\\.craftbukkit\\.(?<version>" + VERSION_PATTERN + ")\\.");
    
    private static @NullOr CraftBukkitVersion SERVER;
    
    /**
     * Gets the internal package version of the server.
     *
     * @return the server's internal package version
     */
    public static CraftBukkitVersion server()
    {
        if (SERVER == null)
        {
            String serverClassName = Bukkit.getServer().getClass().getCanonicalName();
            Matcher matcher = PACKAGE_VERSION_PATTERN.matcher(serverClassName);
            
            SERVER = (matcher.find())
                ? parseCraftBukkitVersion(matcher.group("version"))
                    .orElseThrow(() ->
                        new IllegalStateException("Cannot resolve version: " + matcher.group("version"))
                    )
                : new CraftBukkitVersion(MinecraftVersion.server());
        }
        
        return SERVER;
    }
    
    /**
     * Parses the input text into a Minecraft version.
     *
     * <p>A valid version is: {@code v{major}_{minor}_R{patch}}, like: {@code v1_18_R1}.</p>
     *
     * @param text  input text
     *
     * @return the parsed version if successful, otherwise empty
     */
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
    
    /**
     * Constructs.
     * The arguments are renamed to reflect the slight changes in format compared to a standard version.
     *
     * @param release   release version number
     * @param major     major version number
     * @param revision  revision version number
     */
    public CraftBukkitVersion(int release, int major, int revision)
    {
        super(release, major, revision);
        this.packageVersion = "v" + release + "_" + major + "_R" + revision;
        this.craftBukkitPackage = "org.bukkit.craftbukkit." + packageVersion;
        this.minecraftPackage = (atLeast(1, 17)) ? "net.minecraft" : "net.minecraft.server." + packageVersion;
    }
    
    public CraftBukkitVersion(MinecraftVersion version)
    {
        this(version.major(), version.minor(), version.patch());
    }
    
    /**
     * Gets the {@code org.bukkit.craftbukkit} package for this version.
     *
     * @return versioned craftbukkit package
     */
    public String craftBukkitPackage() { return craftBukkitPackage; }
    
    /**
     * Creates a fully-qualified class name from this version's {@code org.bukkit.craftbukkit} package.
     *
     * @param className     class name (with any required subpackages)
     *
     * @return fully-qualified class name within the versioned craftbukkit package
     */
    public String craftBukkitClass(String className) { return craftBukkitPackage + "." + className; }
    
    /**
     * Gets the NMS package for this version, or simply {@code net.minecraft} if 1.17 or above.
     *
     * @return versioned NMS package
     */
    public String minecraftPackage() { return minecraftPackage; }
    
    /**
     * Creates a fully-qualified class name from this version's {@code net.minecraft} (NMS) package.
     *
     * @param className     class name (with any required subpackages)
     *
     * @return fully-qualified class name within the versioned NMS package
     */
    public String minecraftClass(String className) { return minecraftPackage + "." + className; }
    
    @Override
    public String toString()
    {
        return packageVersion;
    }
}
