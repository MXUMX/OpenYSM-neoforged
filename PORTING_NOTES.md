# OpenYSM macOS Port Notes

## Summary

A working macOS Apple Silicon port of `ysm-2.6.4-neoforge+mc1.21.1-release.jar` could not be completed from the released jar alone.

The blocking issue is not just a platform check. The mod's 1.21.1 NeoForge builds depend on a closed native C++/JNI core for startup, model parsing, sync processing, texture upload, and rendering. The jar does not include a macOS native library, and there is no Java fallback implementation in the shipped artifact.

## Main Findings

### 1. The released jar does not contain a macOS native library

The current jar ships these native files:

- `META-INF/native/ysm-core.dll`
- `META-INF/native/libysm-core.so`
- `META-INF/native/libysm-core-android.so`

There is no `libysm-core.dylib`.

### 2. The loader rejects unsupported platforms only after trying two native load paths

The native loader lives in:

- `/tmp/ysm-new-cfr/com/elfmcys/openysm/O0OO0o0oo0oooOO0o000o0oO.java`

Important details:

- It already supports loading a manually supplied native library through the `YSM_CORE_LIB` environment variable.
- If `YSM_CORE_LIB` is absent, it extracts an embedded native from `META-INF/native/`.
- The embedded-native path supports:
  - Windows AMD64
  - Linux AMD64
  - Android AArch64
- It does not support macOS because the jar does not include a macOS build and the loader has no macOS extraction branch.

### 3. The current mod depends on JNI for core behavior

Examples of current native entry points:

- `/tmp/ysm-new-cfr/com/elfmcys/openysm/O0OoO00O0OOO00oOooooO0oO.java`
  - common init / shutdown
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/OOoO000OoOOoOO00oOoOo0OO.java`
  - client init
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/OO00OOOO0O0OoOO0OooOo000.java`
  - model registry / sync processing
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/O0o000Ooo0o0OO0oOoOoOO0O.java`
  - vertex rendering
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/oo0OooO00oO0ooOooO000OOO.java`
  - texture upload / release
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/o0oo0oOO0OooOoo0ooo00o00.java`
  - audio decode helpers

This means a loader-only patch would not be enough.

### 4. The Java side still contains useful data holders

Several classes are plain Java containers and wrappers:

- `/tmp/ysm-new-cfr/com/elfmcys/openysm/oo00OoOoOoOooooO00OOOO0o.java`
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/OoOO00oo00oOOOO00oOo0000.java`
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/oO0oo00O0o000Oo0000OoOo0.java`
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/oo0oO0ooOo0oO0Oooo00Ooo0.java`
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/oO00ooO0ooO0OO0OoOOO0ooO.java`
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/OO0OoOO0oOOo0ooO0o0o00oo.java`
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/oOoOo0OOOo0oo0oooOoOOO0o.java`
- `/tmp/ysm-new-cfr/com/elfmcys/openysm/O0O0oooo0Oo0o0000OOo0ooo.java`

So the object model is partly visible. The missing part is the native code that creates and fills those structures from pack data and network payloads, then renders/uploads them.

### 5. Older 1.21.1 NeoForge versions are not a usable pure-Java base

I inspected these older jars too:

- `downloads/openysm-neoforge-1.21-2.3.2-hotfix.jar`
- `downloads/openysm-neoforge-1.21-2.4.1-release.jar`
- `downloads/ysm-2.5.1-neoforge+mc1.21.1-release.jar`
- `downloads/ysm-2.6.0-neoforge+mc1.21.1-release.jar`

Result:

- `2.3.2`, `2.4.1`, `2.5.1`, `2.6.0`, and `2.6.4` all already use the native-core architecture.
- The older pure-Java era ended before these 1.21.1 NeoForge builds.

### 6. The current pack format is understandable, but not sufficient by itself

The built-in models inside the jar are readable JSON-folder packs under:

- `assets/openysm/builtin/...`

Examples:

- `assets/openysm/builtin/default/ysm.json`
- `assets/openysm/builtin/default/models/main.json`
- `assets/openysm/builtin/default/animations/main.animation.json`

These packs use a `spec: 2` format. That is enough to understand the asset layout, but not enough to reconstruct the full runtime behavior without rebuilding the missing native pipeline.

## What Would Actually Be Required

One of these paths is needed for a real macOS port:

1. Obtain the upstream source for the native core and build a macOS `libysm-core.dylib`.
2. Clean-room reimplement the native JNI surface in C/C++ for macOS.
3. Replace the native-backed pipeline with a new Java implementation that:
   - parses current YSM packs,
   - matches the current model registry/sync protocol,
   - uploads textures,
   - renders meshes identically enough to stay multiplayer-compatible.

## Practical Next Step

If upstream source becomes available, the first step should be:

- extend the loader in `O0OO0o0oo0oooOO0o000o0oO` to extract and load `libysm-core.dylib` on macOS, or simply rely on the already-supported `YSM_CORE_LIB` environment variable for testing.

Until then, there is no honest way to produce a working Apple Silicon port from this jar alone.
