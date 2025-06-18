"""Sphinx configuration file for RunTheNumbers' documentation."""

import glob
import os
import pathlib
import re
import sys

# -- General configuration ------------------------------------------------------------

extensions = [
    "myst_parser",
    "sphinxcontrib.plantuml",
]

project = "RunTheNumbers"
copyright = "The pip developers"
version = "1.0.0b1"
release = version

# -- Options for myst-parser ----------------------------------------------------------

myst_enable_extensions = ["deflist", "attrs_block", "colon_fence", "strikethrough"]
myst_heading_anchors = 3

# -- Options for HTML -----------------------------------------------------------------

html_theme = "furo"
html_title = f"{project} documentation v{release}"

# Disable the generation of the various indexes
html_use_modindex = False
html_use_index = False
