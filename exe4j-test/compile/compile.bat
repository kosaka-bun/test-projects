set PROJECT_NAME="exe4j-test"

cd ..
del /s /q .\build\libs\*
call gradlew bootJar
cd compile

copy "%PROJECT_NAME%.exe4j" ..\build\libs\
cd ..\build\libs
exe4jc "%PROJECT_NAME%.exe4j"
