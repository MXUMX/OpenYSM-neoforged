#!/usr/bin/env bash
set -euo pipefail

unzip -q -o priv-storage/openysm-ci-libs.zip

if find src/main/resources/assets/openysm/builtin -type f -print -quit >/dev/null 2>&1; then
  exit 0
fi

builtin_dir=""

for candidate in \
  "src/main/resources/assets/openysm/builtin" \
  "assets/openysm/builtin" \
  "openysm/builtin" \
  "builtin"
do
  if find "$candidate" -type f -print -quit >/dev/null 2>&1; then
    builtin_dir="$candidate"
    break
  fi
done

if [ -z "$builtin_dir" ]; then
  builtin_file=$(find . -path "*/assets/openysm/builtin/*" -type f -print -quit)

  if [ -n "$builtin_file" ]; then
    builtin_dir="${builtin_file%%/assets/openysm/builtin/*}/assets/openysm/builtin"
  fi
fi

if [ -n "$builtin_dir" ] && [ "$builtin_dir" != "src/main/resources/assets/openysm/builtin" ]; then
  rm -rf src/main/resources/assets/openysm/builtin
  mkdir -p src/main/resources/assets/openysm
  cp -R "$builtin_dir" src/main/resources/assets/openysm/builtin
fi
