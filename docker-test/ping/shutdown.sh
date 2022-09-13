echo 'stop1';
echo 'stop2';
kill -s TERM $(pidof app);
echo 'after stop'