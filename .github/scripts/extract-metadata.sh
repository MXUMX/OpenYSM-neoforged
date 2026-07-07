#!/usr/bin/env bash
set -euo pipefail

mod_name=$(grep "^mod_name=" gradle.properties | cut -d'=' -f2-)
mod_version=$(grep "^mod_version=" gradle.properties | cut -d'=' -f2-)
minecraft_version=$(grep "^minecraft_version=" gradle.properties | cut -d'=' -f2-)

{
  echo "mod_name=$mod_name"
  echo "mod_version=$mod_version"
  echo "minecraft_version=$minecraft_version"
} >> "$GITHUB_OUTPUT"
