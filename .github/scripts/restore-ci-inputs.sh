#!/usr/bin/env bash
set -euo pipefail

archive="priv-storage/openysm-ci-libs.zip"

if [ ! -f "$archive" ]; then
  echo "::error::Missing private compile-only archive: $archive"
  exit 1
fi

tmp_dir=$(mktemp -d)
cleanup() {
  rm -rf "$tmp_dir"
}
trap cleanup EXIT

unzip -q -o "$archive" -d "$tmp_dir"

copy_file() {
  local rel_path="$1"
  local file_name
  local source_path

  file_name=$(basename "$rel_path")

  if [ -f "$tmp_dir/$rel_path" ]; then
    source_path="$tmp_dir/$rel_path"
  else
    source_path=$(find "$tmp_dir" -type f -name "$file_name" -print -quit)
  fi

  if [ -n "${source_path:-}" ]; then
    mkdir -p "$(dirname "$rel_path")"
    cp "$source_path" "$rel_path"
  fi
}

copy_openysm_libs() {
  local source_dir=""

  for candidate in \
    "$tmp_dir/work/reference/OpenYSM/libs" \
    "$tmp_dir/reference/OpenYSM/libs" \
    "$tmp_dir/OpenYSM/libs"
  do
    if find "$candidate" -name "*.jar" -print -quit >/dev/null 2>&1; then
      source_dir="$candidate"
      break
    fi
  done

  if [ -n "$source_dir" ]; then
    mkdir -p work/reference/OpenYSM/libs
    cp "$source_dir"/*.jar work/reference/OpenYSM/libs/
  fi
}

copy_file "work/reference/neoforge-1.21.1/carryon-neoforge-1.21.1-2.2.4.4.jar"
copy_file "work/reference/neoforge-1.21.1/immersive_melodies-neoforge-0.6.4+1.21.1.jar"
copy_file "work/reference/neoforge-1.21.1/ParCool-1.21.1-3.4.3.3-NF.jar"
copy_file "work/reference/neoforge-1.21.1/accessories-neoforge-1.1.0-beta.53+1.21.1.jar"
copy_file "work/reference/neoforge-1.21.1/simplehats-neoforge-1.21.1-0.4.0.jar"
copy_openysm_libs
