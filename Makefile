# The suite, run against any Teyru compiler.
#
#   make                       # everything
#   make TEYRU=/path/to/teyru   # the same, with a compiler you built
#   make programs              # one part
#
# `go test` in the compiler repository drives the same files; this is for
# running them without it. See run.sh for what each part is.
TEYRU ?= teyru

.PHONY: all programs packages diagnostics native java-compat check

all:
	TEYRU=$(TEYRU) sh run.sh

programs:
	TEYRU=$(TEYRU) sh run.sh programs

packages:
	TEYRU=$(TEYRU) sh run.sh packages

diagnostics:
	TEYRU=$(TEYRU) sh run.sh diagnostics

native:
	TEYRU=$(TEYRU) sh run.sh native

java-compat:
	TEYRU=$(TEYRU) sh run.sh java-compat

# check runs the parser over the scripts, so a typo is caught without a compiler.
check:
	sh -n run.sh
