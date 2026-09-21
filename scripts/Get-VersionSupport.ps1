param(
    [string]$OutputPath = (Join-Path $PSScriptRoot '../release/version-support.json')
)

$ErrorActionPreference = 'Stop'
$headers = @{ 'User-Agent' = 'Jungdab/JungdabsTurrets (version support audit)' }
$manifest = Invoke-RestMethod 'https://piston-meta.mojang.com/mc/game/version_manifest_v2.json' -Headers $headers
$geckolib = Invoke-RestMethod 'https://api.modrinth.com/v2/project/geckolib/version' -Headers $headers
$existing = Invoke-RestMethod 'https://api.modrinth.com/v2/project/TYLHPOeu/version' -Headers $headers
$targets = @($manifest.versions | Where-Object {
    $_.type -eq 'release' -and
    ($_.id -match '^1\.21\.([1-9]|10|11)$' -or $_.id -match '^26\.[123](\.\d+)?$')
} | Sort-Object { [version]$_.id })

$rows = foreach ($target in $targets) {
    foreach ($loader in @('fabric', 'forge', 'neoforge')) {
        $candidates = @($geckolib | Where-Object {
            $_.game_versions -contains $target.id -and $_.loaders -contains $loader
        } | Sort-Object date_published -Descending)
        $stable = @($candidates | Where-Object version_type -eq 'release')
        $dependency = if ($stable.Count) { $stable[0] } elseif ($candidates.Count) { $candidates[0] } else { $null }
        $published = @($existing | Where-Object {
            $_.game_versions -contains $target.id -and $_.loaders -contains $loader
        })
        [ordered]@{
            minecraft = $target.id
            loader = $loader
            already_published = ($published.Count -gt 0)
            geckolib = if ($dependency) { $dependency.version_number } else { $null }
            geckolib_version_id = if ($dependency) { $dependency.id } else { $null }
            geckolib_release_type = if ($dependency) { $dependency.version_type } else { $null }
            status = if ($published.Count) { 'already-published' }
                     elseif (!$dependency) { 'dependency-not-listed' }
                     else { 'port-and-test-required' }
        }
    }
}

$report = [ordered]@{
    checked_at = [DateTimeOffset]::UtcNow.ToString('o')
    modrinth_project = 'TYLHPOeu'
    curseforge_project = 1595544
    note = 'Dependency listings are candidates only, not proof of binary compatibility. Missing listings require upstream investigation. Never publish an untested game-version label.'
    sources = @('https://piston-meta.mojang.com/mc/game/version_manifest_v2.json',
                'https://api.modrinth.com/v2/project/geckolib/version',
                'https://api.modrinth.com/v2/project/TYLHPOeu/version')
    targets = @($rows)
}
$fullPath = [IO.Path]::GetFullPath($OutputPath)
[IO.Directory]::CreateDirectory([IO.Path]::GetDirectoryName($fullPath)) | Out-Null
[IO.File]::WriteAllText($fullPath, ($report | ConvertTo-Json -Depth 8), [Text.UTF8Encoding]::new($false))
$rows | ForEach-Object { [pscustomobject]$_ } | Format-Table minecraft, loader, geckolib, status
