# Teyru — the end-to-end suite

Every `.teyru` program the compiler is expected to compile, run, and get the
same answer out of.

## How it is used

It runs itself, against any Teyru compiler, with nothing but a shell:

```sh
TEYRU=/path/to/teyru sh run.sh          # everything
TEYRU=/path/to/teyru sh run.sh programs # one part
TEYRU=/path/to/teyru make               # the same, through the Makefile
TEYRU=/path/to/teyru TEYRU_TARGET=linux/arm64 sh run.sh   # for another platform
```

`run.sh` builds each case, runs it, and compares with the file beside it. It
needs no Go, no compiler source tree and no test harness, which is the point:
the cases are data, and a data repository that can only be read by one program
in one other repository is not a project of its own.

`TEYRU_TARGET` is the platform the whole run is for, spelled `<os>/<arch>` the
way `teyru build --target` spells it; empty (the default) is the machine the run
is on. It is passed to every build, and it is also the platform the `.skip` files
are read against -- a case is skipped when the program cannot run *there*, since
that is where the run is taking it. The compiler repository's `go test` driver
reads exactly the same variable the same way (`TEYRU_TARGET=linux/arm64 go test
./...`), so a cross run means one thing to both drivers and not two. Nothing else
about the run changes: the cases, the expectations and the policy files are the
ones below.

Running for another platform only says something where the programs can actually
run on it: `linux/arm64` needs a registered `binfmt_misc` handler or `qemu-user`
(`QEMU_LD_PREFIX` pointing at the target's sysroot), and `windows/amd64` needs
wine. `CC` is a separate variable, and it is `run.sh`'s own C compiler for
`native/net_c_test.c` -- a cross run needs that one set to the target's compiler
as well.

The compiler repository mounts this one as a submodule at `tests/` and drives
the same files through `go test`, which is the nicer entry point when you are
already in that repository because it names each failing case individually:

```sh
git clone --recurse-submodules https://github.com/teyru-lang/Teyru
cd Teyru
make test          # unit tests, the programs, the packages and the diagnostics
make test-programs # just the programs, with the compiler's own output shown
```

`TestPrograms` reads `programs/`, `TestPackages` reads `packages/`,
`TestDiagnostics` reads `diagnostics/`, `TestNative` reads `native/`, and the
module system's tests read `modules/`. Both drivers read the same files: the
data is the contract, and the two are not allowed to disagree about what a
`.expected` means.

Three more files shape what a case means, and both drivers read each of them
the same way:

| File | Read by | Meaning |
|---|---|---|
| `
## A test that lands before its code

The tests repository is a submodule, so a test pushed here is *in the suite* the
moment any compiler pull request moves the pointer -- and every landing moves it to
the tip. That means a test pushed before the change it tests makes **every other
landing red**, which is exactly what happened twice on 2026-09-17 (t196_string_bytes
and t180_http_gzip, both waiting on the string work, and a full gate that failed on
nothing else).

So: a test pushed to `main` here before its code is in `main` of the compiler must
carry an entry in `known-failures.txt` in the **same commit**, naming the work item
and saying it landed first. The mechanism then removes the entry when the code
arrives -- a listed case that passes fails the run -- so the entry cannot outlive
the gap it describes. The alternative, a test left on a branch, is worse: the suite
is the thing that tells us a change is correct, and a test nobody runs tells us
nothing.
known-failures.txt` | both | `<case> <work item> <reason>`: this case fails today and `Wn` is going to fix it |
| `<part>/<case>.skip` | both | the platforms the case cannot run on: `windows`, `darwin/arm64`, or `!linux` for the one platform it *can*; everything after `#` is the reason |
| `jdk-diff-allow.txt` | `TestJDKDiff` | `<case> <kind> <work item> <reason>`: a difference from the JDK that is decided, or that Java cannot express; `kind` is `java` when javac rejects the printed Java and `out` when the two answers differ |

A case listed in `known-failures.txt` that **passes** fails the run, so an entry
cannot outlive the bug it describes: when the work item lands, the entry is
deleted with it. The same is true of `jdk-diff-allow.txt`: an entry whose case
now agrees with the JDK fails the run. Neither file accepts an entry without a
reason, and `jdk-diff-allow.txt` accepts no entry without a work item or an
explicit `none` -- a difference nobody has looked at is not a difference that
has been accepted.

## Layout

| Path | What it holds |
|---|---|
| `programs/` | `name.teyru` and `name.expected`: the program's stdout, compared byte for byte |
| `packages/` | whole directories of packages; `expected` is the output of the program in it, `error` the diagnostic it must be rejected with |
| `modules/` | `teyru.mod` fixtures, a local module cache under `fixtures/`, and the programs that exercise `teyru get` and `mod tidy` |
| `diagnostics/` | `name.teyru` and `name.code`: the program must be *rejected*, and the diagnostic named in `.code` must appear. The cases live here rather than as string literals in the compiler's test source, because a suite that keeps one of its parts inside the compiler is the compiler testing itself |
| `native/` | the C implementation a `native` method gets linked against, plus `net_c_test.c`: the socket layer tested as C, because the failures that matter need a peer that is genuinely slow, silent or gone |

### The files beside a program

| Suffix | Meaning |
|---|---|
| `.teyru` | the program |
| `.expected` | the program's stdout, compared byte for byte |
| `.args` | the program's arguments, split on whitespace: one per line, or several on a line. An argument cannot contain a space, which is how both drivers read the file |
| `.exit` | the status the program must end with, when it is not 0 |
| `.experr` | the program's stderr, compared byte for byte when the file is there |
| `.java.ref` | the same program written in Java, which is where the expected output came from |
| `.skip` | the platforms the case cannot run on (see the table above) |

## Adding a program

Write `programs/t146_something.teyru`, run it, and save its output as
`programs/t146_something.expected`. Take the next free number; there is no other
bookkeeping. Because the comparison includes the exit status, a program that is
supposed to fail needs a `.exit` file, and one that writes to stderr needs
`.experr`. A program that must not compile does not belong here at all: both
drivers require a `.expected` beside every program, and a case that is supposed
to be rejected is a `diagnostics/` case (with the `.code` file naming the
diagnostic) or a `packages/` case (with an `error` file).

When the point of a program is that Teyru and Java agree, keep the Java next to
it as `name.java.ref` (javac compiles it, run it, and use its output as
`.expected`) — that is how the standard library is held to the JDK's answers.

A rejection case goes in `diagnostics/`: the program, and a `.code` file naming
the diagnostic it must be rejected with. If the rejection needs more than one
file, it belongs in `packages/` with an `error` file instead, which is also
where a case that must be *accepted* but needs a package tree goes (with
`expected`).

## The JDK differential

`programs/t2??_probe_*` are the Java semantics probes: each one is a program
written twice, once in Teyru and once in `name.java.ref`, and the `.expected`
is what the JDK printed when it ran the Java. `TestJDKDiff` (in the compiler
repository, `TEYRU_JDK=<jdk 21 home> make jdk-diff`) goes further: it translates
every translatable program with `teyru emit-java`, compiles the translation with
javac, and compares stdout and exit status with this compiler's answer. A
program `emit-java` refuses is not compared, and the refusal says why.

Appendix C of the fix plan is `t230_probe_core` and `t231_probe_strings`, with
`t215`-`t219` adding the formatting, boxing, collection, exception and Unicode
families. They are the red half of W5, W6 and W7: each one fails here until the
work item it names in `known-failures.txt` lands.

## Licence

GPL-2.0-only, the same as the compiler. See `LICENSE`.
