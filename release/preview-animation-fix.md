# Turret inventory preview animation regression

Reported during the Minecraft 26.1 family client tests on 2026-09-23.

## Reproduction and cause

The 26.1.2 build using official GeckoLib 5.5.2 stopped all three inventory
previews at animation time 0.0. The preview entities' tick counts still
advanced from 9 to 24 to 64. World turrets rendered normally.

The vanilla `InventoryScreen.extractEntityInInventoryFollowsMouse` helper
extracts entity state with `partialTick = 1`. GeckoLib 5.5.2 treats that
value as a paused animation. The same diagnostic run on 26.1 with GeckoLib
5.5 advanced normally, so the missing entity placement flag was not the
cause of the frozen first frame.

## Fix

The turret screen now extracts its preview render state using the actual
frame partial tick and submits it to the GUI entity renderer. Preview
bounds, scale, mouse rotation and existing placement animation behavior
are preserved. World rendering and gameplay are unchanged.

Applied on `codex/minecraft-26.1`, `codex/minecraft-26.1.1`, and
`codex/minecraft-26.1.2`; the 26.2 and 26.3 branches inherit the fix.

## Verification

Both actual clients passed on 26.1, 26.1.1, 26.1.2, 26.2 and 26.3. Tests sample
animation progress at early, middle and completed stages for all three
turrets, then close and reopen every screen and repeat the checks.
Each loader checks six screen openings. The 26.1.2 NeoForge and Fabric
screenshots also show the turret unfolding between sampled frames.

The 26.1.2 NeoForge basic preview progressed approximately 0.399 → 1.149
→ 1.499 seconds. Fabric progressed 0.100 → 0.899 → 1.499 seconds on its
initial opening. Previously every sample was 0.0. Completed sniper and
tank animations reach their own longer end times.

Logs reside in each version worktree: `preview-regression.log` for 26.1
and 26.1.1; `preview-before.log`, `preview-after-neo.log` and
`preview-after-fabric.log` for 26.1.2. Test code remains outside production
JARs. Artifact hashes and publication state are in `verification.json`.

The 26.2 results are in `port-client-2.log`, and the 26.3 results are in
`port-build-runtime.log`. Both loaders' temporal screenshots were reviewed.
The newer ports also require a nonzero ID for preview entities and a valid
numeric idle keyframe in the sniper model; all four animations for each
turret are checked for successful baking before exercising their screens.
