#!/bin/bash
cd `dirname $0`
cd ..

# custom config
JVM_CONFIG_FILE="/wls/appsystems/jvm_config.txt"
MAIN_CLASS=org.curriculumdesign.bookserp.BooksErpApplication
ROOT_CONFIG_FILE="bootstrap.properties"
# default setting
HEAP_SIZE="-Xms1g -Xmx1g"



#####################################################
APP_HOME=`pwd`
JAVA_BIN="/wls/jdk/bin/java"
LOG_DIR=$APP_HOME/logs
CONF_DIR=$APP_HOME/conf
LIB_DIR=$APP_HOME/lib

PID=""
P1=$1
IS_OK=0

# the spring config file env
P2=$2
SPRING_ENV=""

if [ -n "$P2" ]; 
	then
		SPRING_ENV=" -Dspring.profiles.active=$P2 "
		echo "use config $SPRING_ENV"
fi

JVM_PARAMS=`sed '/cdteam-cqip-main-server/!d;s/.*=//' $JVM_CONFIG_FILE | tr -d '\r'`
JMX_PORT=`sed '/jmx.port/!d;s/.*=//' conf/$ROOT_CONFIG_FILE | tr -d '\r'`
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
export CLASSPATH=.:$CONF_DIR:$LIB_JARS

if [ -n "$JVM_PARAMS" ]; 
	then
		HEAP_SIZE=$JVM_PARAMS
		echo "set heap size $HEAP_SIZE"
fi

if [ -z $JAVA_BIN ]; 
	then
		echo "Can not find the java environment!"
		exit 1
fi

findAppPid()
{
	PID=`ps -ef | grep java | grep "$APP_HOME" | awk '{print $2}'`
	return $PID
}

appIsRunning()
{
	echo "Check application status..."
	findAppPid
	echo "The application pid is: $PID"
	if [ -n "$PID" ]
	then
		return 1
	fi

	return 0
}

status()
{
	appIsRunning
	RS=$?
	if [[ "$RS" == 1 ]]; 
		then
			echo "Application is already running."
		else
			echo "Application is not running."
	fi
}

start()
{
	appIsRunning
	RS=$?
	if [[ "$RS" == 1 ]]; then
		echo "Application is already running."
		exit 0
	fi

	echo "start now"
	JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPV4Stack=true -Dlog.dir=$LOG_DIR "
	JAVA_MEM_OPTS=" -server $HEAP_SIZE -XX:+PrintGCTimeStamps -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=6 -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+PrintGCDetails -XX:NewRatio=3 -XX:SurvivorRatio=8 -XX:+CMSScavengeBeforeRemark "
	JMX_OPTS=" -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
	nohup $JAVA_BIN $SPRING_ENV $JAVA_OPTS $JAVA_MEM_OPTS $JMX_OPTS $MAIN_CLASS > /dev/null 2>&1 &
	sleep 1
	findAppPid
	echo "The application pid is: $PID"
	return 1
}

stop()
{
	appIsRunning
	RS=$?
	if [[ "$RS" == 0 ]]; then
		echo "Application is not running."
		exit 0
	fi

	echo "stop now"
	kill -15 $PID 2>/dev/null

	HAV_KILL=1
	for i in {1..10}; do
		findAppPid
		if [ -n "$PID" ]
			then
				echo -ne "."
              	sleep 1
			else
				HAV_KILL=0
		fi
	done
	
	if [[ $HAV_KILL == 1 ]]; 
		then
			echo "Can not stop the application!"
			return 0
	fi

	return 1
}

restart()
{
	appIsRunning
	RS=$?
	if [[ "$RS" == 1 ]]; 
		then
			# if app is running. first stop it,then start it.
			stop
			RS_STOP=$?

			if [[ "$RS_STOP" == 1 ]]; 
				then
					start
					RS_START=$?

					return $RS_START
				else
					echo "Stop the application failure!"
					return 0
			fi
		else
			# if app is not runnint. so start it.
			start
			RS_START=$?
			return $RS_START
	fi

	return 1
}

runStep()
{
	case $1 in
        start)
          	echo "The application start running..."
          	start
          	IS_OK=$?
        ;;
        stop)
          	echo "The application stop..."
          	stop
          	IS_OK=$?
        ;;
        restart)
			echo "The application restart running..."
			restart
			IS_OK=$?
		;;
		status)
			status
			exit 0
		;;
        *)
          echo "Unsupport cmd!"
          echo "Please enter parameters. like start|stop|restart|status"
        ;;
	esac

	if [[ "$IS_OK" == 1 ]]; 
		then
			echo $1 "success!"
		else
			echo $1 "failure!"
	fi

	exit 0
}

if [ -z "$1" ]; 
	then
		echo "Please enter parameters. like start|stop|restart|status"
		exit 1
	else
		runStep $P1
fi

exit 0