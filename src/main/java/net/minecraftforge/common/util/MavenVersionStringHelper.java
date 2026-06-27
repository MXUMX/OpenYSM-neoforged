package net.minecraftforge.common.util;

import org.apache.maven.artifact.versioning.ArtifactVersion;

public final class MavenVersionStringHelper {
    private MavenVersionStringHelper() {
    }

    public static String artifactVersionToString(ArtifactVersion version) {
        return version == null ? "" : version.toString();
    }
}
