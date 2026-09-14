# Teyru — the end-to-end suite

Every `.teyru` program the compiler is expected to compile, run, and get the
same answer out of. This repository is data: the harness that drives it lives in
the compiler repository, [`teyru-lang/Teyru`](https://github.com/teyru-lang/Teyru).

## How it is used

The compiler repository mounts this one as a submodule at `tests/`:

```sh
git clone --recurse-submodules https://github.com/teyru-lang/Teyru
cd Teyru
make test          # unit tests, the programs, the packages and the diagnostics
make test-programs # just the programs, with the compiler's own output shown
```

`go test .` in the compiler repository runs `TestPrograms` (everything under
`programs/`), `TestPackages` (everything under `packages/`) and `TestDiagnostics`
(the programs that must be *rejected*); the module system's tests read
`modules/`. Nothing here is compiled on its own — a suite without the compiler
has nothing to run.

## Layout

| Path | What it holds |
|---|---|
| `programs/` | `name.teyru` and `name.expected`: the program's stdout and stderr as one stream, compared byte for byte |
| `packages/` | whole directories of packages; `expected` is the output of the program in it, `error` the diagnostic it must be rejected with |
| `modules/` | `teyru.mod` fixtures, a local module cache under `fixtures/`, and the programs that exercise `teyru get` and `mod tidy` |
| `native/` | the C implementation a `native` method gets linked against |

### The files beside a program

| Suffix | Meaning |
|---|---|
| `.teyru` | the program |
| `.expected` | what it prints, exit status included (stdout and stderr together) |
| `.args` | one command-line argument per line, when the program takes any |
| `.exit` | the status the program must end with, when it is not 0 |
| `.experr` | a diagnostic the *compiler* must refuse the program with instead of running it |
| `.java.ref` | the same program written in Java, which is where the expected output came from |

## Adding a program

Write `programs/t146_something.teyru`, run it, and save its output as
`programs/t146_something.expected`. Take the next free number; there is no other
bookkeeping. Because the comparison includes the exit status, a program that is
supposed to fail needs a `.exit` file, and one that must not compile needs
`.experr` and no `.expected`.

When the point of a program is that Teyru and Java agree, keep the Java next to
it as `name.java.ref` (javac compiles it, run it, and use its output as
`.expected`) — that is how the standard library is held to the JDK's answers.

## Licence

GPL-2.0-only, the same as the compiler. See `LICENSE`.
