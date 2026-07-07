#!/usr/bin/env bash
set -euo pipefail

target_dir="src/main/resources/assets/openysm/builtin"

has_builtin_files() {
  find "$target_dir" -type f -print -quit >/dev/null 2>&1
}

copy_builtin_dir() {
  local source_dir="$1"

  rm -rf "$target_dir"
  mkdir -p "$(dirname "$target_dir")"
  cp -R "$source_dir" "$target_dir"
}

copy_builtin_contents() {
  local source_dir="$1"

  rm -rf "$target_dir"
  mkdir -p "$target_dir"

  for item in "$source_dir"/misc "$source_dir"/default "$source_dir"/wine_fox; do
    [ -e "$item" ] || continue
    cp -R "$item" "$target_dir/"
  done
}

unzip -q -o priv-storage/openysm-ci-libs.zip

if has_builtin_files; then
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

if [ -n "$builtin_dir" ] && [ "$builtin_dir" != "$target_dir" ]; then
  copy_builtin_dir "$builtin_dir"
fi

if has_builtin_files; then
  exit 0
fi

for content_root in "." "priv-storage"; do
  if [ -f "$content_root/misc/ysm-pack.json" ] || [ -f "$content_root/default/ysm.json" ] || [ -f "$content_root/wine_fox/ysm-pack.json" ]; then
    copy_builtin_contents "$content_root"
    break
  fi
done

if has_builtin_files; then
  exit 0
fi

archive_entries=$(zipinfo -1 priv-storage/openysm-ci-libs.zip 2>/dev/null | wc -l | tr -d ' ')
builtin_like_entries=$(zipinfo -1 priv-storage/openysm-ci-libs.zip 2>/dev/null | grep -E -c '(^|/)(builtin|assets/openysm)(/|$)' || true)
archive_sha256=$(sha256sum priv-storage/openysm-ci-libs.zip | cut -d' ' -f1)

echo "::error::Bundled OpenYSM resources were not restored from priv-storage/openysm-ci-libs.zip."
echo "Archive entries: $archive_entries; builtin-like entries: $builtin_like_entries; sha256: $archive_sha256"
exit 1
