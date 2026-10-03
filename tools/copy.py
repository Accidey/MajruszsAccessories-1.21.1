import os, re, shutil, sys

ROOT = r"D:\Games\MOD\饰品"
LIB_COMMON = os.path.join(ROOT, "MajruszLibrary-1.20.X", "common", "src", "main", "java", "com", "majruszlibrary")
LIB_NF = os.path.join(ROOT, "MajruszLibrary-1.20.X", "neoforge", "src", "main", "java", "com", "majruszlibrary")
ACC_COMMON = os.path.join(ROOT, "MajruszsAccessories-1.20.X", "common", "src", "main", "java", "com", "majruszsaccessories")
ACC_NF = os.path.join(ROOT, "MajruszsAccessories-1.20.X", "neoforge", "src", "main", "java", "com", "majruszsaccessories")
OUT = os.path.join(ROOT, "src", "main", "java", "com", "xulai")

DROP = {
    "mixininterfaces/IMixinClientLevel.java",
    "mixininterfaces/IMixinEntity.java",
}

EXTRA = [
    "mixin/IMixinBreedGoal.java",
    "mixin/IMixinCriteriaTriggers.java",
    "mixin/MixinAbstractHorse.java",
    "mixin/MixinAnimal.java",
    "mixin/MixinBrewingStandBlockEntity.java",
    "mixin/MixinBrushableBlockEntity.java",
    "mixin/MixinClientLevel.java",
    "mixin/MixinCommands.java",
    "mixin/MixinCreativeModeTab.java",
    "mixin/MixinEntity.java",
    "mixin/MixinExperienceOrb.java",
    "mixin/MixinFishingHook.java",
    "mixin/MixinFoxBreedGoal.java",
    "mixin/MixinGuiGraphics.java",
    "mixin/MixinInventoryMenu.java",
    "mixin/MixinItem.java",
    "mixin/MixinItemStack.java",
    "mixin/MixinKeyboardHandler.java",
    "mixin/MixinLivingEntity.java",
    "mixin/MixinMinecraft.java",
    "mixin/MixinMinecraftServer.java",
    "mixin/MixinMob.java",
    "mixin/MixinPlayer.java",
    "mixin/MixinPlayerList.java",
    "mixin/MixinReloadableServerResources.java",
    "mixin/MixinServerLevel.java",
    "mixin/MixinTamableAnimal.java",
    "mixin/MixinVillager.java",
    "mixin/MixinWanderingTrader.java",
    "mixininterfaces/IMixinClientLevel.java",
    "mixininterfaces/IMixinEntity.java",
]

EXTRA_NF = [
    "mixin/neoforge/MixinAquaFishingBobberEntity.java",
    "mixin/neoforge/MixinLootTable.java",
    "registry/RegistryNeoForge.java",
    "modhelper/DataNeoForge.java",
    "modhelper/ResourceNeoForge.java",
    "network/NetworkNeoForge.java",
    "platform/IntegrationNeoForge.java",
    "platform/SideNeoForge.java",
    "item/ItemNeoForge.java",
    "events/OnBreakSpeedGetNeoForge.java",
    "events/OnEntitySwimSpeedMultiplierGetNeoForge.java",
    "events/OnItemFishedNeoForge.java",
    "events/OnGameInitializedNeoForge.java",
    "events/OnGuiOverlaysRegisteredNeoForge.java",
]

PKG_MAP = [
    (r"com\.majruszlibrary", "com.xulai.majruszlibrary"),
    (r"com\.majruszsaccessories", "com.xulai.majruszsaccessories"),
    (r"net\.minecraftforge\.api\.distmarker", "net.neoforged.api.distmarker"),
    (r"net\.minecraftforge\.eventbus\.api", "net.neoforged.bus.api"),
    (r"net\.minecraftforge\.fml", "net.neoforged.fml"),
    (r"net\.minecraftforge\.client\.ForgeHooksClient", "net.neoforged.neoforge.client.ForgeHooksClient"),
    (r"net\.minecraftforge\.client\.event", "net.neoforged.neoforge.client.event"),
    (r"net\.minecraftforge\.client\.gui\.overlay", "net.neoforged.neoforge.client.gui.overlay"),
    (r"net\.minecraftforge\.common\.MinecraftForge", "net.neoforged.neoforge.common.NeoForge"),
    (r"net\.minecraftforge\.common", "net.neoforged.neoforge.common"),
    (r"net\.minecraftforge\.event", "net.neoforged.neoforge.event"),
    (r"net\.minecraftforge\.network", "net.neoforged.neoforge.network"),
    (r"net\.minecraftforge\.registries", "net.neoforged.neoforge.registries"),
    (r"net\.minecraftforge\.server", "net.neoforged.neoforge.server"),
    (r"net\.minecraftforge", "net.neoforged.neoforge"),
]

def apply_pkg(text):
    out = text
    for pat, rep in PKG_MAP:
        out = re.sub(pat, rep, out)
    out = re.sub(r"\bMinecraftForge\.EVENT_BUS\b", "NeoForge.EVENT_BUS", out)
    return out

def copy(src_root, rels, dest_root):
    n = 0
    for rel in rels:
        src = os.path.join(src_root, rel.replace("/", os.sep))
        if not os.path.isfile(src):
            print("MISSING:", src)
            continue
        dst = os.path.join(dest_root, rel.replace("/", os.sep))
        os.makedirs(os.path.dirname(dst), exist_ok=True)
        with open(src, encoding="utf-8") as f:
            text = f.read()
        with open(dst, "w", encoding="utf-8", newline="\n") as f:
            f.write(apply_pkg(text))
        n += 1
    return n

lib_rels = []
with open(os.path.join(ROOT, "tools", "needed.txt"), encoding="utf-8") as f:
    for line in f:
        rel = line.strip()
        if rel and rel not in DROP:
            lib_rels.append(rel)
for rel in EXTRA:
    if rel not in lib_rels:
        lib_rels.append(rel)

acc_rels = []
for base in (ACC_COMMON, ACC_NF):
    for dirpath, _, files in os.walk(base):
        for fn in files:
            if fn.endswith(".java"):
                rel = os.path.relpath(os.path.join(dirpath, fn), base).replace("\\", "/")
                acc_rels.append(rel)

dest_lib = os.path.join(OUT, "majruszlibrary")
dest_acc = os.path.join(OUT, "majruszsaccessories")

if os.path.exists(OUT):
    shutil.rmtree(OUT)

n1 = copy(LIB_COMMON, lib_rels, dest_lib)
extra_nf_missing = []
n2 = 0
for rel in EXTRA_NF:
    src = os.path.join(LIB_NF, rel.replace("/", os.sep))
    if not os.path.isfile(src):
        extra_nf_missing.append(rel)
        continue
    dst = os.path.join(dest_lib, rel.replace("/", os.sep))
    os.makedirs(os.path.dirname(dst), exist_ok=True)
    with open(src, encoding="utf-8") as f:
        text = f.read()
    with open(dst, "w", encoding="utf-8", newline="\n") as f:
        f.write(apply_pkg(text))
    n2 += 1
n3 = copy(ACC_COMMON, acc_rels, dest_acc)

print("library copied:", n1, "+ neoforge:", n2)
print("accessories copied:", n3)
if extra_nf_missing:
    print("MISSING NF:", extra_nf_missing)
