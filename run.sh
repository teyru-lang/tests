#!/bin/sh
# The end-to-end suite, driven from this repository alone.
#
#   TEYRU=/path/to/teyru sh run.sh              # everything
#   TEYRU=teyru sh run.sh programs diagnostics  # named parts
#   TEYRU=teyru RUNS_KEEP=0 sh run.sh           # drop the built programs
#   TEYRU=teyru TEYRU_TARGET=linux/arm64 sh run.sh   # build and run for another
#                                               # platform this machine can run
#
# Nothing here needs Go, the compiler's source tree, or its test harness: this
# repository is the suite, and this script is what runs it. The compiler
# repository drives the same files through `go test` (TestPrograms, TestPackages,
# TestDiagnostics, TestNative), which gives per-case output inside `go test`;
# both read the same data, and the data is the contract.
#
# A part is a directory:
#
#   programs/     NAME.teyru, and beside it NAME.expected (stdout, byte for
#                 byte), NAME.args (whitespace-separated arguments), NAME.exit
#                 (the status it must end with, when not 0), NAME.experr (stderr,
#                 when the program is expected to write any)
#   packages/     one directory per case, compiled as a whole: `expected` is the
#                 program's stdout, or `error` names the diagnostic it must be
#                 rejected with instead
#   diagnostics/  NAME.teyru and NAME.code: the program must be *rejected*, and
#                 the diagnostic in NAME.code must appear
#   native/       program.teyru with its native methods implemented in impl.c,
#                 expected.txt for the output, and net_c_test.c, which tests the
#                 socket layer as C because that is the only way to have a peer
#                 that is genuinely slow, silent or gone
#   java-compat/  NAME.java and NAME.expected: the same program written in Java,
#                 compiled by this compiler as it stands, with the expectation
#                 taken from running it on the JDK
#
# Two files beside them shape what a failure means, and the compiler's own
# driver reads both the same way:
#
#   known-failures.txt   NAME WORK REASON: this case fails today and work item
#                        WORK is going to fix it. A listed case that passes
#                        fails the run, so the entry cannot outlive the bug.
#   NAME.skip            the platforms the case cannot run on: `windows`,
#                        `darwin/arm64`, or `!linux` for the one platform it
#                        can. Everything after `#` is the reason.
set -u

TEYRU=${TEYRU:-teyru}
TARGET=${TEYRU_TARGET:-}
RUNS_KEEP=${RUNS_KEEP:-0}
here=$(cd "$(dirname "$0")" && pwd)
cd "$here"

if ! command -v "$TEYRU" >/dev/null 2>&1 && [ ! -x "$TEYRU" ]; then
  echo "run.sh: no compiler: TEYRU=$TEYRU is not on PATH and not a file" >&2
  echo "        set TEYRU to a teyru binary, or install one" >&2
  exit 2
fi

CC=${CC:-cc}
parts=$*
[ -n "$parts" ] || parts="programs packages diagnostics native java-compat"

# build passes everything through to the compiler, adding the target when one was
# named. TEYRU_TARGET is the one place this script says which platform it is
# building for, and it is said here rather than at each call site so that a build
# added later cannot forget it. The compiler repository's driver reads the same
# variable the same way (see README.md), because the two drivers are not allowed
# to disagree about what a run means.
build() {
  if [ -n "$TARGET" ]; then
    "$TEYRU" build --target "$TARGET" "$@"
  else
    "$TEYRU" build "$@"
  fi
}

tmp=$(mktemp -d "${TMPDIR:-/tmp}/teyru-suite.XXXXXX")
trap 'if [ "$RUNS_KEEP" = "0" ]; then rm -rf "$tmp"; else echo "run.sh: kept $tmp"; fi' EXIT

pass=0
fail=0
known=0
skipped=0

ok() {
  pass=$((pass + 1))
  printf 'PASS %s\n' "$1"
}

# bad prints the reason and counts the case as failed. It is called with the case
# name first, then whatever the reason is, so a failure is one line to read.
bad() {
  fail=$((fail + 1))
  printf 'FAIL %s\n' "$1"
  shift
  for line in "$@"; do
    printf '     %s\n' "$line"
  done
}

# kf_entry NAME prints "WORK|REASON" when NAME is in known-failures.txt.
kf_entry() {
  [ -f known-failures.txt ] || return 1
  awk -v name="$1" '
    { line = $0; sub(/#.*/, "", line)
      n = split(line, f, /[ \t]+/)
      if (f[1] != name) next
      reason = ""
      for (i = 3; i <= n; i++) reason = reason (i > 3 ? " " : "") f[i]
      print f[2] "|" reason
      exit
    }' known-failures.txt
}

# verdict NAME PROBLEMS DETAIL...
#
# One case's outcome, with known-failures.txt applied: a listed case that fails
# is a known failure and does not fail the run, and a listed case that passes is
# a failure of the run, because the entry is now a lie.
verdict() {
  name=$1
  problems=$2
  shift 2
  entry=$(kf_entry "$name" || true)
  if [ -n "$entry" ]; then
    work=${entry%%|*}
    reason=${entry#*|}
    if [ -z "$problems" ]; then
      fail=$((fail + 1))
      printf 'FAIL %s\n' "$name"
      printf '     listed in known-failures.txt as %s (%s), but it passed: delete the entry\n' "$work" "$reason"
    else
      known=$((known + 1))
      printf 'KNOWN %s\n' "$name"
      printf '     %s (%s): %s\n' "$work" "$reason" "$problems"
    fi
    return
  fi
  if [ -n "$problems" ]; then
    bad "$name" "$problems" "$@"
  else
    ok "$name"
  fi
}

# ---------------------------------------------------------------- platforms
#
# NAME.skip names the platforms a case cannot run on, and the compiler's driver
# reads the same file the same way: `windows`, `darwin/arm64`, or `!linux` for
# the one platform it *can* run on. uname spells both halves differently from
# Go, so both are normalised here.
#
# When TEYRU_TARGET names a platform, that platform is the one a case is skipped
# for: the programs are built for it, so what they can do is what that platform
# can do. A cross run is only meaningful where the programs can actually run --
# qemu for linux/arm64, wine for windows/amd64 -- and a case that platform
# cannot run is then skipped for the platform's reason rather than the host's.
if [ -n "$TARGET" ]; then
  goos=${TARGET%%/*}
  goarch=${TARGET#*/}
else
  goos=$(uname -s 2>/dev/null | tr 'A-Z' 'a-z')
  case "$goos" in
    mingw*|msys*|cygwin*|windows*) goos=windows ;;
    darwin) goos=darwin ;;
    linux) goos=linux ;;
  esac
  goarch=$(uname -m 2>/dev/null)
  case "$goarch" in
    x86_64|amd64) goarch=amd64 ;;
    aarch64|arm64) goarch=arm64 ;;
    i386|i686) goarch=386 ;;
  esac
fi

# skip_reason NAME prints the reason when NAME cannot run on this platform, and
# prints nothing when it can.
skip_reason() {
  f="$1.skip"
  [ -f "$f" ] || return 0
  awk -v here="$goos/$goarch" -v os="$goos" '
    { line = $0
      if (i = index(line, "#")) { r = substr(line, i + 1); gsub(/^[ \t]+|[ \t]+$/, "", r); reason = reason (reason == "" ? "" : "; ") r; line = substr(line, 1, i - 1) }
      n = split(line, tok, /[ \t]+/)
      for (j = 1; j <= n; j++) {
        t = tok[j]
        if (t == "") continue
        neg = (substr(t, 1, 1) == "!")
        if (neg) t = substr(t, 2)
        if ((t == os || t == here) != neg) { print reason; exit }
      }
    }' "$f"
}

# skip_if_platform NAME returns 0 when the case must be skipped here.
skip_if_platform() {
  why=$(skip_reason "$1")
  [ -n "$why" ] || return 1
  skipped=$((skipped + 1))
  printf 'SKIP %s\n' "$1"
  printf '     not this platform (%s/%s): %s\n' "$goos" "$goarch" "$why"
  return 0
}

# ---------------------------------------------------------------- programs
if [ "$parts" = "programs packages diagnostics native java-compat" ] || [ "${parts#*programs}" != "$parts" ]; then
  for src in programs/*.teyru; do
    [ -f "$src" ] || continue
    name=$(basename "$src" .teyru)
    if skip_if_platform "programs/$name"; then continue; fi
    want="programs/$name.expected"
    if [ ! -f "$want" ]; then
      verdict "$name" "missing $want"
      continue
    fi
    if ! build -O1 -o "$tmp/$name" "$src" >"$tmp/$name.cc" 2>&1; then
      verdict "$name" "compile failed" "$(sed -n '1,4p' "$tmp/$name.cc")"
      continue
    fi
    set -- ""
    if [ -f "programs/$name.args" ]; then
      # The arguments are split on whitespace, the way the harness that reads
      # these files in the compiler repository splits them; an argument cannot
      # contain a space.
      set -- $(cat "programs/$name.args")
    fi
    "$tmp/$name" "$@" >"$tmp/$name.out" 2>"$tmp/$name.err"
    code=$?
    wantexit=0
    if [ -f "programs/$name.exit" ]; then
      wantexit=$(cat "programs/$name.exit")
    fi
    problems=""
    [ "$code" = "$wantexit" ] || problems="exit $code, want $wantexit"
    cmp -s "$want" "$tmp/$name.out" || problems="$problems stdout differs from $want"
    if [ -f "programs/$name.experr" ]; then
      cmp -s "programs/$name.experr" "$tmp/$name.err" || problems="$problems stderr differs from programs/$name.experr"
    fi
    if [ -n "$problems" ]; then
      verdict "$name" "$problems" "$(diff "$want" "$tmp/$name.out" 2>/dev/null | sed -n '1,6p')"
    else
      verdict "$name" ""
    fi
  done
fi

# ---------------------------------------------------------------- packages
if [ "$parts" = "programs packages diagnostics native java-compat" ] || [ "${parts#*packages}" != "$parts" ]; then
  for dir in packages/*/; do
    [ -d "$dir" ] || continue
    name=$(basename "$dir")
    if skip_if_platform "packages/$name"; then continue; fi
    if [ -f "$dir/error" ]; then
      want=$(cat "$dir/error")
      if build -O0 -o "$tmp/pkg-$name" "$dir" >"$tmp/pkg-$name.out" 2>&1; then
        verdict "$name" "expected a compile failure, got none"
      elif grep -qF "$want" "$tmp/pkg-$name.out"; then
        verdict "$name" ""
      else
        verdict "$name" "expected $want in the diagnostics" "$(sed -n '1,4p' "$tmp/pkg-$name.out")"
      fi
      continue
    fi
    want="$dir/expected"
    if [ ! -f "$want" ]; then
      verdict "$name" "missing $want"
      continue
    fi
    if ! build -O1 -o "$tmp/pkg-$name" "$dir" >"$tmp/pkg-$name.cc" 2>&1; then
      verdict "$name" "compile failed" "$(sed -n '1,4p' "$tmp/pkg-$name.cc")"
      continue
    fi
    "$tmp/pkg-$name" >"$tmp/pkg-$name.out" 2>/dev/null
    if cmp -s "$want" "$tmp/pkg-$name.out"; then
      verdict "$name" ""
    else
      verdict "$name" "stdout differs from $want" "$(diff "$want" "$tmp/pkg-$name.out" | sed -n '1,6p')"
    fi
  done
fi

# ---------------------------------------------------------------- diagnostics
if [ "$parts" = "programs packages diagnostics native java-compat" ] || [ "${parts#*diagnostics}" != "$parts" ]; then
  for src in diagnostics/*.teyru; do
    [ -f "$src" ] || continue
    name=$(basename "$src" .teyru)
    if skip_if_platform "diagnostics/$name"; then continue; fi
    wantf="diagnostics/$name.code"
    if [ ! -f "$wantf" ]; then
      verdict "$name" "missing $wantf, the diagnostic it must be rejected with"
      continue
    fi
    want=$(cat "$wantf")
    if build -O0 -o "$tmp/diag-$name" "$src" >"$tmp/diag-$name.out" 2>&1; then
      verdict "$name" "expected a compile failure, got none"
    elif grep -qF "$want" "$tmp/diag-$name.out"; then
      verdict "$name" ""
    else
      verdict "$name" "expected $want in the diagnostics" "$(sed -n '1,4p' "$tmp/diag-$name.out")"
    fi
  done
fi

# ---------------------------------------------------------------- native
if [ "$parts" = "programs packages diagnostics native java-compat" ] || [ "${parts#*native}" != "$parts" ]; then
  if [ -f native/program.teyru ]; then
    if skip_if_platform "native/program"; then :; else
    mkdir -p "$tmp/native"
    if build -O1 -o "$tmp/native/program" \
        --native native/impl.c --native-header "$tmp/native/native.h" \
        --cc-flag -I --cc-flag "$tmp/native" native/program.teyru >"$tmp/native/cc" 2>&1; then
      "$tmp/native/program" >"$tmp/native/out" 2>&1
      if cmp -s native/expected.txt "$tmp/native/out"; then
        # The header is the contract the C side has to meet: every function it
        # declares must be defined by the C the program was linked against, or
        # the program links against something else.
        declared=$(sed -n 's/.*\(tyn_[A-Za-z0-9_]*\)(.*/\1/p' "$tmp/native/native.h" | sort -u)
        missing=""
        for fn in $declared; do
          grep -qF "$fn(" native/impl.c || missing="$missing $fn"
        done
        if [ -n "$missing" ]; then
          verdict "native/program" "the generated header declares functions the C does not define:$missing"
        else
          verdict "native/program" ""
        fi
      else
        verdict "native/program" "stdout differs from native/expected.txt" "$(diff native/expected.txt "$tmp/native/out" | sed -n '1,6p')"
      fi
    else
      verdict "native/program" "compile failed" "$(sed -n '1,4p' "$tmp/native/cc")"
    fi
    fi
  fi
  if [ -f native/net_c_test.c ]; then
    if skip_if_platform "native/net_c_test"; then :; else
    if $CC -O1 -std=gnu11 -Wall -Wextra -o "$tmp/native/net_c_test" native/net_c_test.c >"$tmp/native/c.out" 2>&1; then
      "$tmp/native/net_c_test" >"$tmp/native/c.run" 2>&1
      if grep -q '0 failure(s)' "$tmp/native/c.run"; then
        verdict "native/net_c_test" ""
      else
        verdict "native/net_c_test" "the C test reported failures" "$(tail -3 "$tmp/native/c.run")"
      fi
    else
      verdict "native/net_c_test" "the C test did not compile" "$(sed -n '1,4p' "$tmp/native/c.out")"
    fi
    fi
  fi
fi

# ------------------------------------------------------------- java-compat
#
# Unmodified Java programs: NAME.java, and NAME.expected holding what the JDK
# 21 printed when it ran the same file (`javac -d out NAME.java && java -cp out
# NAME`; the expectation is never hand-written). The compiler reads .java as a
# source extension and the statement terminator is optional (decision D6), so a
# case here fails when this compiler refuses Java that javac accepts, or when
# the two answers differ. That is the subset `docs`' "Java source compiles
# unchanged" names, and the size of the subset is the number of files here.
if [ "$parts" = "programs packages diagnostics native java-compat" ] || [ "${parts#*java-compat}" != "$parts" ]; then
  for src in java-compat/*.java; do
    [ -f "$src" ] || continue
    name=$(basename "$src" .java)
    if skip_if_platform "java-compat/$name"; then continue; fi
    want="java-compat/$name.expected"
    if [ ! -f "$want" ]; then
      verdict "$name" "missing $want, the output the JDK printed"
      continue
    fi
    if ! build -O1 -o "$tmp/jc-$name" "$src" >"$tmp/jc-$name.cc" 2>&1; then
      verdict "$name" "compile failed" "$(sed -n '1,4p' "$tmp/jc-$name.cc")"
      continue
    fi
    "$tmp/jc-$name" >"$tmp/jc-$name.out" 2>"$tmp/jc-$name.err"
    code=$?
    problems=""
    [ "$code" = "0" ] || problems="exit $code, and the JDK's run of it ended 0"
    cmp -s "$want" "$tmp/jc-$name.out" || problems="$problems stdout differs from $want"
    if [ -n "$problems" ]; then
      verdict "$name" "$problems" "$(diff "$want" "$tmp/jc-$name.out" 2>/dev/null | sed -n '1,6p')" "$(sed -n '1,4p' "$tmp/jc-$name.err")"
    else
      verdict "$name" ""
    fi
  done
fi

printf '\n%d passed, %d failed, %d known, %d skipped\n' "$pass" "$fail" "$known" "$skipped"
[ "$fail" -eq 0 ]
