#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CANONICAL="$ROOT_DIR/libs/openysm-forge-2.6.6.jar"
BUILT="$ROOT_DIR/build/libs/openysm-forge-2.6.6.jar"

if [[ ! -f "$CANONICAL" ]]; then
  echo "Missing canonical jar: $CANONICAL" >&2
  exit 1
fi

if [[ ! -f "$BUILT" ]]; then
  echo "Missing built jar: $BUILT" >&2
  exit 1
fi

canonical_hash="$(shasum -a 256 "$CANONICAL" | awk '{print $1}')"
built_hash="$(shasum -a 256 "$BUILT" | awk '{print $1}')"

if [[ "$canonical_hash" != "$built_hash" ]]; then
  echo "Jar hash mismatch" >&2
  echo "canonical $canonical_hash  $CANONICAL" >&2
  echo "built     $built_hash  $BUILT" >&2
  exit 1
fi

echo "Verified byte-identical jar: $built_hash"
