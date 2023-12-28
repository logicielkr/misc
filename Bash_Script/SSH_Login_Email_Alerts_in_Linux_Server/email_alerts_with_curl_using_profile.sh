#!/bin/bash

if [ -n "$SSH_CONNECTION" -a "$SHLVL" = "1" ]; then
	SENDER="보내는 사람 이메일 주소"
	RECIPIENT="받는 사람 이메일 주소"
	
	remote=$(echo "$SSH_CONNECTION" | awk '{ print $1 }')
	user=$(id -un)
	host="$(hostname)"
	
	subject="SSH Login: $user from $remote on $host"
	message="
Date : $(date +'%Y-%m-%d %H:%M:%S')
SHLVL : $SHLVL
SSH_CONNECTION : $SSH_CONNECTION
	"

	HOST=${RECIPIENT##*@}
	MXFOUND=N
	MXHOST=""

	Result=$( dig ${HOST} mx +short 2> /dev/null | head -1 )
	RETVAL=$?
	if [[ $RETVAL -eq 0 ]]; then
		if [ "$Result" != "" ];then
			MXHOST=$( echo ${Result} | awk '{ print $NF }' )
			MXHOST=${MXHOST%.}
		fi
		MXFOUND=Y
	fi
	
	if [ "$MXHOST" == "" ];then
		Result=$( host -t mx ${HOST} 2> /dev/null | grep mail | head -1 )
		RETVAL=$?
		if [[ $RETVAL -eq 0 ]]; then
			if [ "$Result" != "" ];then
				MXHOST=$( echo ${Result} | awk '{ print $NF }' )
				MXHOST=${MXHOST%.}
			fi
			MXFOUND=Y
		fi
	fi
	
	if [ "$MXHOST" == "" ];then
		Result=$( nslookup -type=mx ${HOST} 2> /dev/null | grep "mail exchanger" | head -1 )
		RETVAL=$?
		if [[ $RETVAL -eq 0 ]]; then
			if [ "$Result" != "" ];then
				MXHOST=$( echo ${Result} | awk '{ print $NF }' )
				MXHOST=${MXHOST%.}
			fi
			MXFOUND=Y
		fi
	fi

	if [[ "$MXHOST" == "" && "$MXFOUND" == "Y" ]]; then
		MXHOST=${HOST}
	fi

	if [ "$MXHOST" == "" ];then
		echo "dig|host|nslookup command not exists!"
		return 100
	else
		curl -s --ssl \
			--url "smtp://${MXHOST}" \
			--mail-from ${SENDER} \
			--mail-rcpt ${RECIPIENT} \
			--upload-file - << EOF
From: <${SENDER}>
To: <${RECIPIENT}>
Subject: ${subject}

${message}

EOF
	
	fi
fi

unset SENDER
unset RECIPIENT
unset remote
unset user
unset host
unset subject
unset message
unset HOST
unset MXFOUND
unset MXHOST
unset Result
unset RETVAL
