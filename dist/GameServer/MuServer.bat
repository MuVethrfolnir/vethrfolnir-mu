@echo off
title Project-Vethrfolnir Mu Game Server 0.9.3-SNAPSHOT
:start
REM -------------------------------------
REM Default parameters for a basic server.
REM leave -server there, that's the JIT initiation
java -Xmx256m -cp ./libs/*; com.vethrfolnir.game.GameSetup
REM
REM If you have a big server and lots of memory, you could experiment for example with
REM java -server -Xmx1536m -Xms1024m -Xmn512m -XX:PermSize=256m -XX:SurvivorRatio=8 -Xnoclassgc -XX:+AggressiveOpts
REM -------------------------------------
if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:restart
echo.
echo Admin Restart ...
echo.
goto start
:error
echo.
echo Server terminated abnormaly
echo.
:end
echo.
echo Server terminated
echo.
pause
