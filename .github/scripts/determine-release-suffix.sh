#!/usr/bin/env bash
set -euo pipefail

existing_count=$(gh api --paginate "/repos/$GITHUB_REPOSITORY/releases?per_page=100" \
  --jq ".[] | select(.tag_name | startswith(\"${MOD_VERSION}-\")) | .tag_name" \
  | wc -l | tr -d ' ')

if [ "$existing_count" -gt 0 ]; then
  suffix="r$((existing_count + 1))"
  jar_version="${MOD_VERSION/-/-${suffix}-}"

  {
    echo "suffix=$suffix"
    echo "tag_name=${MOD_VERSION}-${suffix}-${GITHUB_RUN_ID}.${GITHUB_RUN_ATTEMPT}"
    echo "display_version=${MOD_VERSION} ${suffix}"
    echo "jar_version=$jar_version"
  } >> "$GITHUB_OUTPUT"
else
  {
    echo "suffix="
    echo "tag_name=${MOD_VERSION}-${GITHUB_RUN_ID}.${GITHUB_RUN_ATTEMPT}"
    echo "display_version=${MOD_VERSION}"
    echo "jar_version=${MOD_VERSION}"
  } >> "$GITHUB_OUTPUT"
fi
