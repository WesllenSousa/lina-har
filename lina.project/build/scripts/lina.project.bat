@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

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

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and LINA_PROJECT_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\lina.project-1.0-SNAPSHOT.jar;%APP_HOME%\lib\isax.jar;%APP_HOME%\lib\jahmm-0.6.2.jar;%APP_HOME%\lib\mrmotif.jar;%APP_HOME%\lib\riso.jar;%APP_HOME%\lib\JTattoo-1.6.7.jar;%APP_HOME%\lib\colt-2.1.4.jar;%APP_HOME%\lib\trove4j-3.0.3.jar;%APP_HOME%\lib\gson-2.7.jar;%APP_HOME%\lib\jmotif-gi-1.0.1.jar;%APP_HOME%\lib\jmotif-sax-1.1.2.jar;%APP_HOME%\lib\jtransforms-2.4.0.jar;%APP_HOME%\lib\junit-4.12.jar;%APP_HOME%\lib\trugger-5.1.1.jar;%APP_HOME%\lib\jfreechart-1.0.19.jar;%APP_HOME%\lib\sqlite4java-1.0.392.jar;%APP_HOME%\lib\jcalendar-1.4.jar;%APP_HOME%\lib\org-jdesktop-swingx-1.6-201002261215.nbm;%APP_HOME%\lib\joda-time-2.9.7.jar;%APP_HOME%\lib\slf4j-simple-1.7.23.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\liblinear-2.11.jar;%APP_HOME%\lib\jcip-annotations-1.0.jar;%APP_HOME%\lib\kryo-4.0.1.jar;%APP_HOME%\lib\hppc-0.7.2.jar;%APP_HOME%\lib\JTransforms-3.1.jar;%APP_HOME%\lib\weka-stable-3.8.0.jar;%APP_HOME%\lib\moa-2012.08.jar;%APP_HOME%\lib\concurrent-1.3.4.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\jcommon-1.0.23.jar;%APP_HOME%\lib\com-jhlabs-filters-2.0.235-201002241238.nbm;%APP_HOME%\lib\swingx-1.6.jar;%APP_HOME%\lib\jcommander-1.58.jar;%APP_HOME%\lib\hierarchical-clustering-1.1.0.jar;%APP_HOME%\lib\reflectasm-1.11.3.jar;%APP_HOME%\lib\minlog-1.3.0.jar;%APP_HOME%\lib\objenesis-2.5.1.jar;%APP_HOME%\lib\JLargeArrays-1.5.jar;%APP_HOME%\lib\commons-math3-3.5.jar;%APP_HOME%\lib\java-cup-11b-2015.03.26.jar;%APP_HOME%\lib\java-cup-11b-runtime-2015.03.26.jar;%APP_HOME%\lib\weka-dev-3.9.5.jar;%APP_HOME%\lib\bounce-0.18.jar;%APP_HOME%\lib\mtj-1.0.4.jar;%APP_HOME%\lib\all-1.1.2.pom;%APP_HOME%\lib\netlib-java-1.1.jar;%APP_HOME%\lib\netlib-native_ref-osx-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_ref-linux-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_ref-linux-i686-1.1-natives.jar;%APP_HOME%\lib\netlib-native_ref-win-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_ref-win-i686-1.1-natives.jar;%APP_HOME%\lib\netlib-native_ref-linux-armhf-1.1-natives.jar;%APP_HOME%\lib\native_ref-java-1.1.jar;%APP_HOME%\lib\netlib-native_system-osx-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_system-linux-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_system-linux-i686-1.1-natives.jar;%APP_HOME%\lib\netlib-native_system-linux-armhf-1.1-natives.jar;%APP_HOME%\lib\netlib-native_system-win-x86_64-1.1-natives.jar;%APP_HOME%\lib\netlib-native_system-win-i686-1.1-natives.jar;%APP_HOME%\lib\native_system-java-1.1.jar;%APP_HOME%\lib\core-1.1.2.jar;%APP_HOME%\lib\arpack_combined_all-0.1.jar;%APP_HOME%\lib\sizeofag-1.0.0.jar;%APP_HOME%\lib\filters-2.0.235.jar;%APP_HOME%\lib\swing-worker-1.1.jar;%APP_HOME%\lib\asm-5.0.4.jar;%APP_HOME%\lib\java-cup-11b-20160615.jar;%APP_HOME%\lib\java-cup-runtime-11b-20160615.jar;%APP_HOME%\lib\jfilechooser-bookmarks-0.1.6.jar;%APP_HOME%\lib\jaxb-runtime-2.3.3.jar;%APP_HOME%\lib\istack-commons-runtime-3.0.11.jar;%APP_HOME%\lib\jakarta.xml.bind-api-2.3.3.jar;%APP_HOME%\lib\jakarta.activation-api-1.2.2.jar;%APP_HOME%\lib\jclipboardhelper-0.1.0.jar;%APP_HOME%\lib\txw2-2.3.3.jar;%APP_HOME%\lib\jakarta.activation-1.2.2.jar;%APP_HOME%\lib\jniloader-1.1.jar


@rem Execute lina.project
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %LINA_PROJECT_OPTS%  -classpath "%CLASSPATH%" main.Run %*

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
