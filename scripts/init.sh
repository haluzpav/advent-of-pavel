#!/usr/bin/env shell

# https://pypi.org/project/advent-of-code-data/

session=PASTE_FROM_BROWSER_HERE

# check
aocd --version

aocd_dir="$HOME/.config/aocd"
mkdir "$aocd_dir"
echo $session > "$aocd_dir/token"
