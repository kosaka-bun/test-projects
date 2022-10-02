sh /app.sh &

# 定义退出逻辑
shutdown() {
    sh /shutdown.sh;
    exit 0;
}

trap "shutdown" TERM;

# 持续运行，等待信号
echo 'waiting...';
while true
do
    sleep 1s
done