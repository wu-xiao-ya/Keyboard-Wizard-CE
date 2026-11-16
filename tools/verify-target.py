import hashlib
from pathlib import Path
import struct
import sys
from zipfile import ZipFile


CORE_COMPONENTS = (
    "binding/BindingSearchParser",
    "binding/BindingSearchQuery",
    "binding/DynamicBindingCategories",
    "layout/KeyboardLayoutGeometry",
    "layout/PhysicalKey",
)
CORE_PREFIX = "committee/nova/mkw/core/"
METADATA_FILES = {
    "fabric": "fabric.mod.json",
    "forge": "META-INF/mods.toml",
    "neoforge": "META-INF/neoforge.mods.toml",
}
TEXTURE_PATH = "assets/keyboard_wizard_ce/textures/gui/"


def verify(target):
    properties = dict(
        line.split("=", 1)
        for line in (target / "gradle.properties").read_text(encoding="utf-8").splitlines()
        if "=" in line and not line.lstrip().startswith("#")
    )
    artifact = properties["archives_base_name"] + "-" + properties["mod_version"]
    libs = target / "build/libs"
    binary = libs / (artifact + ".jar")
    sources = libs / (artifact + "-sources.jar")
    loader, version = target.name.split("-", 1)
    metadata = METADATA_FILES[loader]

    with ZipFile(binary) as jar:
        for component in CORE_COMPONENTS:
            jar.getinfo(CORE_PREFIX + component + ".class")
        descriptor = jar.read(metadata).decode("utf-8")
        if "keyboard_wizard_ce" not in descriptor or version not in descriptor:
            raise ValueError(f"Mod id or Minecraft version missing from {metadata}")
        for texture in ("key_wizard_background.png", "screen_toggle_widgets.png"):
            png = jar.read(TEXTURE_PATH + texture)
            if not png.startswith(b"\x89PNG\r\n\x1a\n") or len(png) < 33:
                raise ValueError(f"Invalid PNG resource: {texture}")

    with ZipFile(sources) as jar:
        for component in CORE_COMPONENTS:
            jar.getinfo(CORE_PREFIX + component + ".java")

    checksums = target / "build/release/SHA256SUMS.txt"
    recorded = {
        Path(name.strip()).name: digest
        for digest, name in (
            line.split(maxsplit=1) for line in checksums.read_text(encoding="utf-8").splitlines()
        )
    }
    for jar in (binary, sources):
        digest = hashlib.sha256(jar.read_bytes()).hexdigest()
        if recorded.get(jar.name) != digest:
            raise ValueError(f"SHA-256 mismatch for {jar.name}")
    print(f"Verified {binary.name} and {sources.name}")


if __name__ == "__main__":
    verify(Path(sys.argv[1]))
