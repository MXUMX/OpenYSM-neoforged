# OpenYSM 2.6.6 Forge Reconstruction

This workspace reconstructs the OpenYSM 2.6.6 Forge project from the canonical
`openysm-forge-2.6.6.jar`.

The recovered Java source is under `src/main/java`, and byte-preserved resources,
assets, natives, mixin configs, metadata, and embedded jars are under
`src/main/resources`.

Run:

```sh
./gradlew build
```

The build creates `build/libs/openysm-forge-2.6.6.jar` and verifies that it is
byte-for-byte identical to the canonical jar stored at
`libs/openysm-forge-2.6.6.jar`.
