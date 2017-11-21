@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  lina.project startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and LINA_PROJECT_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\lina.project-1.0-SNAPSHOT.jar;%APP_HOME%\lib\isax.jar;%APP_HOME%\lib\jahmm-0.6.2.jar;%APP_HOME%\lib\mrmotif.jar;%APP_HOME%\lib\riso.jar;%APP_HOME%\lib\JTattoo-1.6.7.jar;%APP_HOME%\lib\colt-2.1.4.jar;%APP_HOME%\lib\trove4j-3.0.3.jar;%APP_HOME%\lib\gson-2.7.jar;%APP_HOME%\lib\junit-4.12.jar;%APP_HOME%\lib\weka-stable-3.8.0.jar;%APP_HOME%\lib\trugger-5.1.1.jar;%APP_HOME%\lib\jfreechart-1.0.19.jar;%APP_HOME%\lib\sqlite4java-1.0.392.jar;%APP_HOME%\lib\jcalendar-1.4.jar;%APP_HOME%\lib\org-jdesktop-swingx-1.6-201002261215.nbm;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\jmotif-sax-1.1.2.jar;%APP_HOME%\lib\jmotif-gi-1.0.1.jar;%APP_HOME%\lib\liblinear-2.11.jar;%APP_HOME%\lib\jtransforms-2.4.0.jar;%APP_HOME%\lib\jcip-annotations-1.0.jar;%APP_HOME%\lib\kryo-4.0.1.jar;%APP_HOME%\lib\hppc-0.7.2.jar;%APP_HOME%\lib\concurrent-1.3.4.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\java-cup-11b-2015.03.26.jar;%APP_HOME%\lib\java-cup-11b-runtime-2015.03.26.jar;%APP_HOME%\lib\bounce-0.18.jar;%APP_HOME%\lib\mtj-1.0.4.jar;%APP_HOME%\lib\arpack_combined_all-0.1.jar;%APP_HOME%\lib\netlib-java-1.1.jar;%APP_HOME%\lib\jcommon-1.0.23.jar;%APP_HOME%\lib\com-jhlabs-filters-2.0.235-201002241238.nbm;%APP_HOME%\lib\swingx-1.6.jar;%APP_HOME%\lib\slf4j-simple-1.7.23.jar;%APP_HOME%\lib\jcommander-1.58.jar;%APP_HOME%\lib\hierarchical-clustering-1.1.0.jar;%APP_HOME%\lib\reflectasm-1.11.3.jar;%APP_HOME%\lib\minlog-1.3.0.jar;%APP_HOME%\lib\objenesis-2.5.1.jar;%APP_HOME%\lib\all-1.1.2.pom;%APP_HOME%\lib\filters-2.0.235.jar;%APP_HOME%\lib\swing-worker-1.1.jar;%APP_HOME%\lib\asm-5.0.4.jar;%APP_HOME%\lib\netlib-native_ref-osx-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_ref-linux-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_ref-linux-i686-1.1-natives.jar;%APP_HOME%\lib\netlib-native_ref-win-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_ref-win-i686-1.1-natives.jar;%APP_HOME%\lib\netlib-native_ref-linux-armhf-1.1-natives.jar;%APP_HOME%\lib\netlib-native_system-osx-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_system-linux-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_system-linux-i686-1.1-natives.jar;%APP_HOME%\lib\netlib-native_system-linux-armhf-1.1-natives.jar;%APP_HOME%\lib\netlib-native_system-win-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_system-win-i686-1.1-natives.jar;%APP_HOME%\lib\native_ref-java-1.1.jar;%APP_HOME%\lib\native_system-java-1.1.jar;%APP_HOME%\lib\jniloader-1.1.jar;%APP_HOME%\lib\joda-time-2.9.7.jar;%APP_HOME%\lib\core-1.1.2.jar

@rem Execute lina.project
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %LINA_PROJECT_OPTS%  -classpath "%CLASSPATH%" main.Run %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable LINA_PROJECT_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%LINA_PROJECT_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
