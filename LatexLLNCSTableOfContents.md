# How to get a TOC with Springer's LLNCS #

Between the `\documentclass` command and the `\title` and `\author` commands, insert the following block of code:

```
% make a proper TOC despite llncs
\setcounter{tocdepth}{2}
\makeatletter
\renewcommand*\l@author[2]{}
\renewcommand*\l@title[2]{}
\makeatletter
```