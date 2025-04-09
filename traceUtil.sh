# bin/bash

# 通过脚本的方式 获取项目里面所有打点的地方

function saveLogToFile() {
  while read line
  do
    value=$(echo $line | grep "TraceUtils.onEvent")
    if [ -n "$value" ]; then
         # shellcheck disable=SC2028
         echo "文件名： $2; \n $value" >> /Users/zhangjianchuan/Desktop/tracePosition.txt
    fi

  done < $1/$2
}

function realTrace(){
  for entry in $(ls $1)
	do
		if test -d $1/$entry;then
          realTrace $1/$entry
        else
          saveLogToFile $1 $entry
        fi
	done
}

function tracePosition(){
	for entry in $(ls $1)
	do
		if test -d $1/$entry/src/main/java;then
        realTrace $1/$entry/src/main/java
    fi
	done
}

tracePosition $PWD