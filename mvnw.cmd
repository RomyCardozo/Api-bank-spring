@REM ============================================================
@REM Maven Wrapper for Windows
@REM Run: mvnw.cmd compile   (no Maven install required)
@REM ============================================================
@echo off
setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
set "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties"

for /F "usebackq tokens=1,2 delims==" %%A in ("%WRAPPER_PROPERTIES%") do (
    if "%%A"=="distributionUrl" set "DISTRIBUTION_URL=%%B"
)

set "DISTRIBUTION_PATH=%USERPROFILE%\.m2\wrapper\dists"

if not exist "%WRAPPER_JAR%" (
    echo Downloading maven-wrapper.jar...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '%WRAPPER_JAR%'"
)

java -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR:~0,-1%" -cp "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
endlocal
