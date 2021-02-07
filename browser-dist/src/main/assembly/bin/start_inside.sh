#!/bin/bash

base_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
base_dir=${base_dir}/..
echo base_dir:${base_dir}
logpath=${base_dir}/logs
configpath=${base_dir}/env_conf
port=8080
profile=prod
enableAuth=true
clusterName=default

show_usage="args: [-e , -l , -p , -s, -c, -n]\
                                  [--spring-profile=, --logdir=, --port=, --enable-security=, --configpath, --clustername]"
#parameters
GETOPT_ARGS=`getopt -o e:l:p:s:c:n: -al spring-profile:,logdir:,port:,enable-security:,configpath:,clustername:, -- "$@"`
eval set -- "$GETOPT_ARGS"
#get parameters
while [ -n "$1" ]
do
        case "$1" in
                -e|--spring-profile) profile=$2; shift 2;;
                -l|--logdir) logpath=$2; shift 2;;
                -p|--port) port=$2; shift 2;;
                -s|--enable-security) enableAuth=$2; shift 2;;
                -c|--configpath) configpath=$2; shift 2;;
                -n|--clustername) clusterName=$2; shift 2;;
                --) break ;;
                *) echo $1,$2,${show_usage}; break ;;
        esac
done

if [[ -z ${profile} ]]; then
        echo ${show_usage}
        echo "spring profile env: $profile , server port: $port , log path: $logpath , enableAuth: $enableAuth"
        exit 0
fi


#prepare java classpath
java_cp=${CLASSPATH}
# add external config path
if [[ -n ${configpath} ]]; then
   java_cp=${configpath}:${java_cp}
fi

java_cp=${java_cp}:${base_dir}/conf
java_cp=${java_cp}:${base_dir}/lib/*

#prepare java options
java_opts="-Xms2048m -Xmx3096m"
java_opts=${java_opts}" -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"
java_opts=${java_opts}" -Dspring.profiles.active=${profile}"
java_opts=${java_opts}" -Dserver.port=${port}"
java_opts=${java_opts}" -Dlog_dir=$logpath"
java_opts=${java_opts}" -DenableAuth=$enableAuth"
java_opts=${java_opts}" -Dapollo.cluster=$clusterName"

# echo java_opts:${java_opts}

echo "start profile env[${profile}] | cluster[${clusterName}] | server.port[${port}] | log path[${logpath}] with security enabled[$enableAuth]"

java ${java_opts} -cp ${java_cp} com.csf.databrowser.DataBrowserApplication
