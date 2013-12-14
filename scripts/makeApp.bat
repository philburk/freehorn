REM prepare release directory for FreeHorn
set APPVSN=040419
set DEVDIR=E:\nomad\Clients\polansky
set APP_RLSDIR=D:\releases\FreeHorn_%APPVSN%
set TEMPDIR=%DEVDIR%\temp

rmdir /Q /S %APP_RLSDIR% >\devnull.txt
mkdir %APP_RLSDIR%

mkdir %TEMPDIR%
cd jsrc
%JDKHOME%\bin\javac -deprecation -classpath %TEMPDIR%;D:\jsyn142_pc_sdk\classes\JSynClasses.jar -d %TEMPDIR% org/frogpeak/horn/*.java
cd ..\

copy D:\jsyn142_pc_sdk\classes\JSynClasses.jar %TEMPDIR%\.
cd %TEMPDIR%
%JDKHOME%\bin\jar xf JSynClasses.jar
%JDKHOME%\bin\jar cfm ../FreeHorn.jar ../jsrc/mainClass.txt org com
cd ..\
rmdir /Q /S %TEMPDIR% >\devnull.txt

copy FreeHorn.jar %APP_RLSDIR%\.
copy docs\index.html %APP_RLSDIR%\.
copy D:\jsyn142_pc_sdk\lib\JSynV142.dll %APP_RLSDIR%\
copy freeHorn.exe %APP_RLSDIR%\.
copy *.png %APP_RLSDIR%\.
