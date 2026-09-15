#!/bin/sh
# The end-to-end suite, driven from this repository alone.
#
#   TEYRU=/path/to/teyru sh run.sh              # everything
#   TEYRU=teyru sh run.sh programs diagnostics  # named parts
#   TEYRU=teyru RUNS_KEEP=0 sh run.sh           # drop the built programs
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
set -u

TEYRU=${TEYRU:-teyru}
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
[ -n "$parts" ] || parts="programs packages diagnostics native"

tmp=$(mktemp -d "${TMPDIR:-/tmp}/teyru-suite.XXXXXX")
trap 'if [ "$RUNS_KEEP" = "0" ]; then rm -rf "$tmp"; else echo "run.sh: kept $tmp"; fi' EXIT

pass=0
fail=0

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

# ---------------------------------------------------------------- programs
if [ "$parts" = "programs packages diagnostics native" ] || [ "${parts#*programs}" != "$parts" ]; then
  for src in programs/*.teyru; do
    [ -f "$src" ] || continue
    name=$(basename "$src" .teyru)
    want="programs/$name.expected"
    if [ ! -f "$want" ]; then
      bad "$name" "missing $want"
      continue
    fi
    if ! "$TEYRU" build -O1 -o "$tmp/$name" "$src" >"$tmp/$name.cc" 2>&1; then
      bad "$name" "compile failed" "$(sed -n '1,4p' "$tmp/$name.cc")"
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
      bad "$name" "$problems" "$(diff "$want" "$tmp/$name.out" 2>/dev/null | sed -n '1,6p')"
    else
      ok "$name"
    fi
  done
fi

# ---------------------------------------------------------------- packages
if [ "$parts" = "programs packages diagnostics native" ] || [ "${parts#*packages}" != "$parts" ]; then
  for dir in packages/*/; do
    [ -d "$dir" ] || continue
    name=$(basename "$dir")
    if [ -f "$dir/error" ]; then
      want=$(cat "$dir/error")
      if "$TEYRU" build -O0 -o "$tmp/pkg-$name" "$dir" >"$tmp/pkg-$name.out" 2>&1; then
        bad "$name" "expected a compile failure, got none"
      elif grep -qF "$want" "$tmp/pkg-$name.out"; then
        ok "$name"
      else
        bad "$name" "expected $want in the diagnostics" "$(sed -n '1,4p' "$tmp/pkg-$name.out")"
      fi
      continue
    fi
    want="$dir/expected"
    if [ ! -f "$want" ]; then
      bad "$name" "missing $want"
      continue
    fi
    if ! "$TEYRU" build -O1 -o "$tmp/pkg-$name" "$dir" >"$tmp/pkg-$name.cc" 2>&1; then
      bad "$name" "compile failed" "$(sed -n '1,4p' "$tmp/pkg-$name.cc")"
      continue
    fi
    "$tmp/pkg-$name" >"$tmp/pkg-$name.out" 2>/dev/null
    if cmp -s "$want" "$tmp/pkg-$name.out"; then
      ok "$name"
    else
      bad "$name" "stdout differs from $want" "$(diff "$want" "$tmp/pkg-$name.out" | sed -n '1,6p')"
    fi
  done
fi

# ---------------------------------------------------------------- diagnostics
if [ "$parts" = "programs packages diagnostics native" ] || [ "${parts#*diagnostics}" != "$parts" ]; then
  for src in diagnostics/*.teyru; do
    [ -f "$src" ] || continue
    name=$(basename "$src" .teyru)
    wantf="diagnostics/$name.code"
    if [ ! -f "$wantf" ]; then
      bad "$name" "missing $wantf, the diagnostic it must be rejected with"
      continue
    fi
    want=$(cat "$wantf")
    if "$TEYRU" build -O0 -o "$tmp/diag-$name" "$src" >"$tmp/diag-$name.out" 2>&1; then
      bad "$name" "expected a compile failure, got none"
    elif grep -qF "$want" "$tmp/diag-$name.out"; then
      ok "$name"
    else
      bad "$name" "expected $want in the diagnostics" "$(sed -n '1,4p' "$tmp/diag-$name.out")"
    fi
  done
fi

# ---------------------------------------------------------------- native
if [ "$parts" = "programs packages diagnostics native" ] || [ "${parts#*native}" != "$parts" ]; then
  if [ -f native/program.teyru ]; then
    mkdir -p "$tmp/native"
    if "$TEYRU" build -O1 -o "$tmp/native/program" \
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
          bad "native/program" "the generated header declares functions the C does not define:$missing"
        else
          ok "native/program"
        fi
      else
        bad "native/program" "stdout differs from native/expected.txt" "$(diff native/expected.txt "$tmp/native/out" | sed -n '1,6p')"
      fi
    else
      bad "native/program" "compile failed" "$(sed -n '1,4p' "$tmp/native/cc")"
    fi
  fi
  if [ -f native/net_c_test.c ]; then
    if $CC -O1 -std=gnu11 -Wall -Wextra -o "$tmp/native/net_c_test" native/net_c_test.c >"$tmp/native/c.out" 2>&1; then
      "$tmp/native/net_c_test" >"$tmp/native/c.run" 2>&1
      if grep -q '0 failure(s)' "$tmp/native/c.run"; then
        ok "native/net_c_test"
      else
        bad "native/net_c_test" "the C test reported failures" "$(tail -3 "$tmp/native/c.run")"
      fi
    else
      bad "native/net_c_test" "the C test did not compile" "$(sed -n '1,4p' "$tmp/native/c.out")"
    fi
  fi
fi

printf '\n%d passed, %d failed\n' "$pass" "$fail"
[ "$fail" -eq 0 ]
