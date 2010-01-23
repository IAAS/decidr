@echo off
set DOCUMENT=seminar
set STYLE=StichwortindexStil
pdflatex -quiet -interaction=nonstopmode %DOCUMENT%.tex
IF EXIST *.ist (makeindex -g -s %STYLE%.ist "%DOCUMENT%.idx") ELSE (echo INFO:     es wird kein Stichwort-Index erstellt)
pdflatex -quiet -interaction=nonstopmode "%DOCUMENT%.tex"
pdflatex -quiet -interaction=nonstopmode "%DOCUMENT%.tex"
pdflatex -quiet -time-statistics -interaction=nonstopmode "%DOCUMENT%.tex"

IF EXIST *.aux (del *.aux)
IF EXIST *.idx (del *.idx)
IF EXIST *.lof (del *.lof)
IF EXIST *.log (del *.log)
IF EXIST *.out (del *.out)
IF EXIST *.toc (del *.toc)
IF EXIST *.ilg (del *.ilg)
IF EXIST *.ind (del *.ind)

PAUSE
