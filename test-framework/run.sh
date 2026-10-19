#!/bin/sh
# The `teyru test` fixture: a project with one passing test, and one with a
# failing test, checked against the lines the command prints and the status it
# exits with.
#
#   TEYRU=/path/to/teyru sh run.sh
#
# Each case is a directory with `expected` (stdout, byte for byte) and `exit`
# (the status the command must end with). The failing case also has a `main`,
# which proves the generated entry point replaces it instead of colliding with
# it: "the package's main ran" must not appear.
set -u
TEYRU=${TEYRU:-teyru}
here=$(cd "$(dirname "$0")" && pwd)
cd "$here"

if ! command -v "$TEYRU" >/dev/null 2>&1 && [ ! -x "$TEYRU" ]; then
  echo "run.sh: no compiler: TEYRU=$TEYRU is not on PATH and not a file" >&2
  exit 2
fi

tmp=$(mktemp -d "${TMPDIR:-/tmp}/teyru-test-framework.XXXXXX")
trap 'rm -rf "$tmp"' EXIT

pass=0
fail=0
for dir in pass fail; do
  want=$(cat "$dir/expected")
  wantcode=$(cat "$dir/exit")
  got=$("$TEYRU" test "$dir" 2>"$tmp/$dir.err")
  code=$?
  problems=""
  [ "$code" = "$wantcode" ] || problems="exit $code, want $wantcode"
  [ "$got" = "$want" ] || problems="$problems stdout differs from $dir/expected"
  if [ -n "$problems" ]; then
    fail=$((fail + 1))
    printf 'FAIL %s\n' "$dir"
    printf '     %s\n' "$problems"
    printf '%s\n' "$got" | sed 's/^/     got: /'
    sed 's/^/     err: /' "$tmp/$dir.err"
  else
    pass=$((pass + 1))
    printf 'PASS %s\n' "$dir"
  fi
done
printf '\n%d passed, %d failed\n' "$pass" "$fail"
[ "$fail" -eq 0 ]
