#!/usr/bin/env bash
set -euo pipefail

missing=0

tracked_files=(
  "src/main/resources/META-INF/jars/ImageStream--SNAPSHOT.jar"
  "src/main/resources/META-INF/jars/architectury-forge-9.2.14.jar"
  "src/main/resources/META-INF/jars/mixinextras-forge-0.3.6.jar"
  "src/main/resources/natives/windows-x64/ysm-core.dll"
  "src/main/resources/natives/linux-x64/libysm-core.so"
  "src/main/resources/natives/macos-x64/libysm-core.dylib"
  "src/main/resources/natives/macos-arm64/libysm-core.dylib"
  "src/main/resources/natives/android-arm64/libysm-core.so"
  "src/main/resources/assets/openysm/builtin/default/ysm.json"
  "src/main/resources/assets/openysm/builtin/misc/ysm-pack.json"
  "src/main/resources/assets/openysm/builtin/wine_fox/ysm-pack.json"
)

restored_files=(
  "work/reference/neoforge-1.21.1/carryon-neoforge-1.21.1-2.2.4.4.jar"
  "work/reference/neoforge-1.21.1/immersive_melodies-neoforge-0.6.4+1.21.1.jar"
  "work/reference/neoforge-1.21.1/ParCool-1.21.1-3.4.3.3-NF.jar"
  "work/reference/neoforge-1.21.1/accessories-neoforge-1.1.0-beta.53+1.21.1.jar"
  "work/reference/neoforge-1.21.1/simplehats-neoforge-1.21.1-0.4.0.jar"
)

for file in "${tracked_files[@]}"; do
  if [ ! -f "$file" ]; then
    echo "::error::Missing tracked build input from repository checkout: $file"
    missing=1
  fi
done

for file in "${restored_files[@]}"; do
  if [ ! -f "$file" ]; then
    echo "::error::Missing restored compile-only jar: $file"
    missing=1
  fi
done

if ! find work/reference/OpenYSM/libs -name "*.jar" -print -quit >/dev/null 2>&1; then
  echo "::error::Missing restored compile-only jars in work/reference/OpenYSM/libs"
  missing=1
fi

if [ "$missing" -ne 0 ]; then
  echo "CI build inputs are incomplete. Tracked runtime inputs should come from the repository; compile-only reference jars should come from priv-storage/openysm-ci-libs.zip."
  exit 1
fi
