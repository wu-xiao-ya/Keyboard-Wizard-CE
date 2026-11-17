import hashlib
import json
from pathlib import Path
import struct
import sys
import tomllib
import zlib
from zipfile import ZipFile


CORE_COMPONENTS = (
    "binding/BindingSearchParser",
    "binding/BindingSearchQuery",
    "binding/DynamicBindingCategories",
    "layout/KeyboardLayoutGeometry",
    "layout/PhysicalKey",
    "gui/PixelKeyboardStyle",
    "gui/ScreenToggleSprite",
)
CORE_PREFIX = "committee/nova/mkw/core/"
MOD_ID = "keyboard_wizard_community_edition"
METADATA_FILES = {
    "fabric": "fabric.mod.json",
    "forge": "META-INF/mods.toml",
    "neoforge": "META-INF/neoforge.mods.toml",
}
SHARED_RESOURCE_DIR = Path(__file__).resolve().parents[1] / "shared-core/src/main/resources"
SHARED_RESOURCES = (
    f"assets/{MOD_ID}/textures/gui/key_wizard_background.png",
    f"assets/{MOD_ID}/textures/gui/screen_toggle_widgets.png",
    "keyboard_wizard_ce_icon.png",
)


def verify_png(png, name):
    if not png.startswith(b"\x89PNG\r\n\x1a\n"):
        raise ValueError(f"Invalid PNG signature: {name}")
    position = 8
    while position + 12 <= len(png):
        size = struct.unpack_from(">I", png, position)[0]
        kind = png[position + 4:position + 8]
        end = position + 8 + size
        if end + 4 > len(png):
            break
        expected = struct.unpack_from(">I", png, end)[0]
        if zlib.crc32(png[position + 4:end]) != expected:
            raise ValueError(f"Invalid PNG chunk checksum in {name}")
        if kind == b"IEND":
            return
        position = end + 4
    raise ValueError(f"Incomplete PNG resource: {name}")


def verify_shared_resources(jar):
    entries = jar.namelist()
    for resource in SHARED_RESOURCES:
        name = f"{jar.filename}: {resource}"
        if entries.count(resource) != 1:
            raise ValueError(f"Expected exactly one copy of {name}")
        png = jar.read(resource)
        verify_png(png, name)
        if png != (SHARED_RESOURCE_DIR / resource).read_bytes():
            raise ValueError(f"Shared resource bytes differ in {name}")

    if not any(name.startswith(f"assets/{MOD_ID}/lang/") and name.endswith((".json", ".lang"))
               for name in entries):
        raise ValueError(f"Missing mod translations in {jar.filename}")


def verify_metadata(jar, metadata, minecraft_version, mod_version):
    descriptor = jar.read(metadata).decode("utf-8")
    if metadata == "fabric.mod.json":
        mod = json.loads(descriptor)
        mod_id = mod["id"]
        minecraft = mod["depends"]["minecraft"]
    elif metadata == "mcmod.info":
        mod, = json.loads(descriptor)
        mod_id = mod["modid"]
        minecraft = mod["mcversion"]
    else:
        data = tomllib.loads(descriptor)
        mod, = data["mods"]
        mod_id = mod["modId"]
        minecraft = next(dep["versionRange"] for dep in data["dependencies"][mod_id]
                         if dep["modId"] == "minecraft")
    if mod_id != MOD_ID or mod["version"] != mod_version or minecraft_version not in minecraft:
        raise ValueError(f"Unexpected mod id, mod version or Minecraft version in {jar.filename}: {metadata}")


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
    metadata = "mcmod.info" if target.name == "forge-1.12.2" else METADATA_FILES[loader]

    with ZipFile(binary) as jar:
        for component in CORE_COMPONENTS:
            jar.getinfo(CORE_PREFIX + component + ".class")
        verify_metadata(jar, metadata, version, properties["mod_version"])
        verify_shared_resources(jar)
        if target.name in {"forge-1.12.2", "forge-1.16.5"}:
            for name in jar.namelist():
                if name.endswith(".class"):
                    major = struct.unpack_from(">H", jar.read(name), 6)[0]
                    if major > 52:
                        raise ValueError(f"Java 8 cannot load {name} (class version {major})")
        if target.name == "forge-1.12.2":
            screen = jar.read("mrnerdy42/keywizard/gui/GuiKeyWizard.class")
            if b"func_73866_w_" not in screen:
                raise ValueError("Forge 1.12.2 JAR is missing the SRG-mapped initGui method")

    with ZipFile(sources) as jar:
        for component in CORE_COMPONENTS:
            jar.getinfo(CORE_PREFIX + component + ".java")
        verify_shared_resources(jar)

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
