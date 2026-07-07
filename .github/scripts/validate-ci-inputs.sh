#!/usr/bin/env bash
set -euo pipefail

missing=0

required_files=(
  "src/main/resources/META-INF/jars/ImageStream--SNAPSHOT.jar"
  "src/main/resources/META-INF/jars/architectury-forge-9.2.14.jar"
  "src/main/resources/META-INF/jars/mixinextras-forge-0.3.6.jar"
  "work/reference/neoforge-1.21.1/carryon-neoforge-1.21.1-2.2.4.4.jar"
  "work/reference/neoforge-1.21.1/immersive_melodies-neoforge-0.6.4+1.21.1.jar"
  "work/reference/neoforge-1.21.1/ParCool-1.21.1-3.4.3.3-NF.jar"
  "work/reference/neoforge-1.21.1/accessories-neoforge-1.1.0-beta.53+1.21.1.jar"
  "work/reference/neoforge-1.21.1/simplehats-neoforge-1.21.1-0.4.0.jar"
)

for file in "${required_files[@]}"; do
  if [ ! -f "$file" ]; then
    echo "::error::Missing compile-only jar: $file"
    missing=1
  fi
done

if ! find work/reference/OpenYSM/libs -name "*.jar" -print -quit >/dev/null 2>&1; then
  echo "::error::Missing compile-only jars in work/reference/OpenYSM/libs"
  missing=1
fi

if ! find src/main/resources/assets/openysm/builtin -type f -print -quit >/dev/null 2>&1; then
  echo "::error::Missing bundled OpenYSM resources in src/main/resources/assets/openysm/builtin"
  missing=1
fi

if [ "$missing" -ne 0 ]; then
  echo "Required local build inputs are missing after restoring priv-storage/openysm-ci-libs.zip."
  exit 1
fi
