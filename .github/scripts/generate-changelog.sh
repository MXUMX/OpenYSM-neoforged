#!/usr/bin/env bash
set -euo pipefail

latest_tag=$(gh api "/repos/$GITHUB_REPOSITORY/releases/latest" --jq ".tag_name" 2>/dev/null || true)

if [ -n "$latest_tag" ] && git rev-parse --verify --quiet "refs/tags/$latest_tag" >/dev/null; then
  commits=$(git log "$latest_tag..HEAD" --pretty=format:"- %h %s")
else
  commits=$(git log --pretty=format:"- %h %s")
fi

if [ -z "$commits" ]; then
  commits="No code changes since the last release."
fi

{
  echo "commits<<EOF"
  echo "$commits"
  echo "EOF"
} >> "$GITHUB_OUTPUT"
