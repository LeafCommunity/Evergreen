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

public class MinecraftVersion implements Comparable<MinecraftVersion>
{
    private static final Pattern VERSION_PATTERN =
        Pattern.compile("(?<major>\\d+)(\\.(?<minor>\\d+))?(\\.(?<patch>\\d+))?");
    
    private static @NullOr MinecraftVersion SERVER;
    
    public static MinecraftVersion server()
    {
        if (SERVER == null)
        {
            SERVER = parseMinecraftVersion(Bukkit.getBukkitVersion()).orElseThrow(() ->
                new IllegalStateException("Cannot resolve version: " + Bukkit.getBukkitVersion())
            );
        }
        return SERVER;
    }
    
    static int intOrZero(@NullOr String input)
    {
        return (input == null) ? 0 : Integer.parseInt(input);
    }
    
    public static Optional<MinecraftVersion> parseMinecraftVersion(String text)
    {
        Objects.requireNonNull(text, "text");
        
        Matcher matcher = VERSION_PATTERN.matcher(text);
        
        return (matcher.find())
            ? Optional.of(new MinecraftVersion(
                intOrZero(matcher.group("major")),
                intOrZero(matcher.group("minor")),
                intOrZero(matcher.group("patch"))
            ))
            : Optional.empty();
    }
    
    private final int major;
    private final int minor;
    private final int patch;
    
    public MinecraftVersion(int major, int minor, int patch)
    {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
    
    public int major() { return major; }
    
    public int minor() { return minor; }
    
    public int patch() { return patch; }
    
    public boolean greaterThan(MinecraftVersion version) { return compareTo(version) > 0; }
    
    public boolean greaterThan(int major) { return compareTo(major, 0, 0) > 0; }
    
    public boolean greaterThan(int major, int minor) { return compareTo(major, minor, 0) > 0; }
    
    public boolean greaterThan(int major, int minor, int patch) { return compareTo(major, minor, patch) > 0; }
    
    public boolean atLeast(MinecraftVersion version) { return compareTo(version) >= 0; }
    
    public boolean atLeast(int major) { return compareTo(major, 0, 0) >= 0; }
    
    public boolean atLeast(int major, int minor) { return compareTo(major, minor, 0) >= 0; }
    
    public boolean atLeast(int major, int minor, int patch) { return compareTo(major, minor, patch) >= 0; }
    
    public boolean atMost(MinecraftVersion version) { return compareTo(version) <= 0; }
    
    public boolean atMost(int major) { return compareTo(major, 0, 0) <= 0; }
    
    public boolean atMost(int major, int minor) { return compareTo(major, minor, 0) <= 0; }
    
    public boolean atMost(int major, int minor, int patch) { return compareTo(major, minor, patch) <= 0; }
    
    public boolean lessThan(MinecraftVersion version) { return compareTo(version) < 0; }
    
    public boolean lessThan(int major) { return compareTo(major, 0, 0) < 0; }
    
    public boolean lessThan(int major, int minor) { return compareTo(major, minor, 0) < 0; }
    
    public boolean lessThan(int major, int minor, int patch) { return compareTo(major, minor, patch) < 0; }
    
    public int compareTo(int major, int minor, int patch)
    {
        int diffMajor = this.major - major;
        if (diffMajor != 0) { return diffMajor; }
        
        int diffMinor = this.minor - minor;
        if (diffMinor != 0) { return diffMinor; }
        
        return this.patch - patch;
    }
    
    @Override
    public int compareTo(MinecraftVersion version)
    {
        return compareTo(version.major, version.minor, version.patch);
    }
    
    @Override
    public String toString()
    {
        return major + "." + minor + "." + patch;
    }
    
    @Override
    public boolean equals(@NullOr Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MinecraftVersion that = (MinecraftVersion) o;
        return major == that.major && minor == that.minor && patch == that.patch;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(major, minor, patch);
    }
}
