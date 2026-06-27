#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
GRADLE_HOME="/Users/mx/.gradle/wrapper/dists/gradle-8.14.4-bin/92wwslzcyst3phie3o264zltu/gradle-8.14.4"

if [[ ! -x "$GRADLE_HOME/bin/gradle" ]]; then
  echo "Missing local Gradle 8.14.4 binary at $GRADLE_HOME/bin/gradle" >&2
  exit 1
fi

export JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"
exec "$GRADLE_HOME/bin/gradle" "$@"
