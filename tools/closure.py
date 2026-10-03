import os, re, sys

ROOT = r"D:\Games\MOD\饰品"
LIB = os.path.join(ROOT, "MajruszLibrary-1.20.X", "common", "src", "main", "java", "com", "majruszlibrary")
LIBNF = os.path.join(ROOT, "MajruszLibrary-1.20.X", "neoforge", "src", "main", "java", "com", "majruszlibrary")
ACC = os.path.join(ROOT, "MajruszsAccessories-1.20.X", "common", "src", "main", "java", "com", "majruszsaccessories")
ACCNF = os.path.join(ROOT, "MajruszsAccessories-1.20.X", "neoforge", "src", "main", "java", "com", "majruszsaccessories")

libfiles = {}
for base in (LIB, LIBNF):
    for dirpath, _, files in os.walk(base):
        for f in files:
            if f.endswith(".java"):
                full = os.path.join(dirpath, f)
                rel = os.path.relpath(full, base).replace("\\", "/")
                pkg = "com.majruszlibrary" + ("." + os.path.dirname(rel).replace("/", ".") if os.path.dirname(rel) else "")
                libfiles[rel] = {"pkg": pkg, "cls": f[:-5], "path": full}

by_pkg = {}
for rel, info in libfiles.items():
    by_pkg.setdefault(info["pkg"], []).append((rel, info["cls"]))

seeds = []
for base in (ACC, ACCNF):
    for dirpath, _, files in os.walk(base):
        for f in files:
            if f.endswith(".java"):
                seeds.append(os.path.join(dirpath, f))

path2rel = {os.path.abspath(i["path"]): r for r, i in libfiles.items()}
included = set()
processed = set()
queue = list(seeds)

def resolve(text, ownpkg):
    found = set()
    for fqn in re.findall(r"import\s+com\.majruszlibrary\.([\w.]+);", text):
        rel = fqn.replace(".", "/") + ".java"
        if rel in libfiles:
            found.add(rel)
    for full in re.findall(r"com\.majruszlibrary\.([\w.]+)", text):
        parts = full.split(".")
        pkgc = "com.majruszlibrary." + ".".join(parts[:-1]) if len(parts) > 1 else "com.majruszlibrary"
        for r, c in by_pkg.get(pkgc, []):
            if c == parts[-1]:
                found.add(r)
    for r, c in by_pkg.get(ownpkg, []):
        if re.search(r"(?<![\w.])%s\b" % re.escape(c), text):
            found.add(r)
    return found

while queue:
    p = queue.pop()
    if p in processed:
        continue
    processed.add(p)
    text = open(p, encoding="utf-8").read()
    rel0 = path2rel.get(os.path.abspath(p))
    if rel0:
        ownpkg = libfiles[rel0]["pkg"]
    else:
        m = re.search(r"^package\s+([\w.]+);", text, re.M)
        ownpkg = m.group(1) if m else ""
    for rel in resolve(text, ownpkg):
        if rel not in included:
            included.add(rel)
            queue.append(libfiles[rel]["path"])

for rel in sorted(included):
    print(rel)
print("\nTOTAL=%d OF %d" % (len(included), len(libfiles)), file=sys.stderr)
