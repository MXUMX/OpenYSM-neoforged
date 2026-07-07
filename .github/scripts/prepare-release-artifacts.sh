#!/usr/bin/env bash
set -euo pipefail

rm -rf release
mkdir -p release

for file in build/libs/*.jar; do
  case "$file" in
    *-sources.jar|*-javadoc.jar) continue ;;
  esac

  base=$(basename "$file" .jar)
  release_base="${base/$MOD_VERSION/$JAR_VERSION}"
  cp "$file" "release/${release_base}.jar"
done

test -n "$(find release -name '*.jar' -print -quit)"
