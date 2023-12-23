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
 * Represents a released version of Minecraft.
 */
@SuppressWarnings("unused")
public class MinecraftVersion implements Comparable<MinecraftVersion>
{
    private static final Pattern VERSION_PATTERN =
        Pattern.compile("(?<major>\\d+)(\\.(?<minor>\\d+))?(\\.(?<patch>\\d+))?");
    
    private static @NullOr MinecraftVersion SERVER;
    
    /**
     * Gets the Minecraft game version of the server.
     *
     * @return the server's game version
     */
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
    
    /**
     * Parses the input text into a Minecraft version.
     *
     * <p>A valid version is: {@code {major}.{minor}.{patch}}, like: {@code 1.18.1}.</p>
     *
     * @param text  input text
     *
     * @return the parsed version if successful, otherwise empty
     */
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
    
    private static int onlyIfPositive(int number, String name)
    {
        if (number < 0) { throw new IllegalArgumentException(name + " is negative: " + number); }
        return number;
    }
    
    private final int major;
    private final int minor;
    private final int patch;
    
    /**
     * Constructs.
     *
     * @param major     major version number
     * @param minor     minor version number
     * @param patch     patch version number
     */
    public MinecraftVersion(int major, int minor, int patch)
    {
        this.major = onlyIfPositive(major, "major");
        this.minor = onlyIfPositive(minor, "minor");
        this.patch = onlyIfPositive(patch, "patch");
    }
    
    /**
     * Gets the major version number.
     *
     * @return major version number
     */
    public int major() { return major; }
    
    /**
     * Gets the minor version number.
     *
     * @return minor version number.
     */
    public int minor() { return minor; }
    
    /**
     * Gets the patch version number.
     *
     * @return patch version number.
     */
    public int patch() { return patch; }
    
    /**
     * Checks if this version is greater than the provided version.
     *
     * @param version   version to compare to
     *
     * @return {@code true} if this version is greater than the provided version
     */
    public boolean greaterThan(MinecraftVersion version) { return compareTo(version) > 0; }
    
    /**
     * Checks if this version is greater than the provided major version number.
     *
     * @param major     major version number
     *
     * @return {@code true} if this version is greater than the provided major version number
     */
    public boolean greaterThan(int major) { return compareTo(major, 0, 0) > 0; }
    
    /**
     * Checks if this version is greater than the provided major and minor version numbers.
     *
     * @param major     major version number
     * @param minor     minor version number
     *
     * @return {@code true} if this version is greater than the provided major and minor version numbers
     */
    public boolean greaterThan(int major, int minor) { return compareTo(major, minor, 0) > 0; }
    
    /**
     * Checks if this version is greater than the provided major, minor, and patch version numbers.
     *
     * @param major     major version number
     * @param minor     minor version number
     * @param patch     patch version number
     *
     * @return {@code true} if this version is greater than the provided major, minor, and patch version numbers
     */
    public boolean greaterThan(int major, int minor, int patch) { return compareTo(major, minor, patch) > 0; }
    
    /**
     * Checks if this version is greater than or equal to the provided version.
     *
     * @param version   version to compare to
     *
     * @return {@code true} if this version is at least the provided version
     */
    public boolean atLeast(MinecraftVersion version) { return compareTo(version) >= 0; }
    
    /**
     * Checks if this version is greater than or equal to the provided major version number.
     *
     * @param major     major version number
     *
     * @return {@code true} if this version is at least the provided major version number
     */
    public boolean atLeast(int major) { return compareTo(major, 0, 0) >= 0; }
    
    /**
     * Checks if this version is greater than or equal to the provided major and minor version numbers.
     *
     * @param major     major version number
     * @param minor     minor version number
     *
     * @return {@code true} if this version is at least the provided major and minor version numbers
     */
    public boolean atLeast(int major, int minor) { return compareTo(major, minor, 0) >= 0; }
    
    /**
     * Checks if this version is greater than or equal to the provided major, minor, and patch version numbers.
     *
     * @param major     major version number
     * @param minor     minor version number
     * @param patch     patch version number
     *
     * @return {@code true} if this version is at least the provided major, minor, and patch version numbers
     */
    public boolean atLeast(int major, int minor, int patch) { return compareTo(major, minor, patch) >= 0; }
    
    /**
     * Checks if this version is less than or equal to the provided version.
     *
     * @param version   version to compare to
     *
     * @return {@code true} if this version is at most the provided version
     */
    public boolean atMost(MinecraftVersion version) { return compareTo(version) <= 0; }
    
    /**
     * Checks if this version is less than or equal to the provided major version number.
     *
     * @param major     major version number
     *
     * @return {@code true} if this version is at most the provided major version number
     */
    public boolean atMost(int major) { return compareTo(major, Integer.MAX_VALUE, Integer.MAX_VALUE) <= 0; }
    
    /**
     * Checks if this version is less than or equal to the provided major and minor version numbers.
     *
     * @param major     major version number
     * @param minor     minor version number
     *
     * @return {@code true} if this version is at most the provided major and minor version numbers
     */
    public boolean atMost(int major, int minor) { return compareTo(major, minor, Integer.MAX_VALUE) <= 0; }
    
    /**
     * Checks if this version is less than or equal to the provided major, minor, and patch version numbers.
     *
     * @param major     major version number
     * @param minor     minor version number
     * @param patch     patch version number
     *
     * @return {@code true} if this version is at most the provided major, minor, and patch version numbers
     */
    public boolean atMost(int major, int minor, int patch) { return compareTo(major, minor, patch) <= 0; }
    
    /**
     * Checks if this version is less than the provided version.
     *
     * @param version   version to compare to
     *
     * @return {@code true} if this version is less than the provided version
     */
    public boolean lessThan(MinecraftVersion version) { return compareTo(version) < 0; }
    
    /**
     * Checks if this version is less than the provided major version number.
     *
     * @param major     major version number
     *
     * @return {@code true} if this version is less than the provided major version number
     */
    public boolean lessThan(int major) { return compareTo(major, 0, 0) < 0; }
    
    /**
     * Checks if this version is less than the provided major and minor version numbers.
     *
     * @param major     major version number
     * @param minor     minor version number
     *
     * @return {@code true} if this version is less than the provided major and minor version numbers
     */
    public boolean lessThan(int major, int minor) { return compareTo(major, minor, 0) < 0; }
    
    /**
     * Checks if this version is less than the provided major, minor, and patch version numbers.
     *
     * @param major     major version number
     * @param minor     minor version number
     * @param patch     patch version number
     *
     * @return {@code true} if this version is less than the provided major, minor, and patch version numbers
     */
    public boolean lessThan(int major, int minor, int patch) { return compareTo(major, minor, patch) < 0; }
    
    /**
     * Compares this version with the provided major, minor, and patch version numbers.
     *
     * @param major     major version number
     * @param minor     minor version number
     * @param patch     patch version number
     *
     * @return comparison in accordance with {@link Comparable#compareTo(Object)}
     * @see #compareTo(MinecraftVersion)
     */
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
