#!/bin/bash

#####session optional pam_exec.so seteuid debug log=/tmp/pam_ssh_email_debug.log /usr/local/bin/pam_exec_email.sh

SENDER="보내는 사람 이메일 주소"
RECIPIENT="받는 사람 이메일 주소"

remote=${PAM_RHOST}
user=${PAM_USER}
host="$(hostname)"
subject=""
if [ ${PAM_TYPE} = "open_session" ]; then
	subject="SSH Login: $user from $remote on $host"
else
	subject="SSH Logout: $user from $remote on $host"
fi

message="
Date : $(date +'%Y-%m-%d %H:%M:%S')
SHLVL : $SHLVL
TTY : ${PAM_TTY}
Service : ${PAM_SERVICE}
PAM_TYPE : ${PAM_TYPE}
User : ${PAM_USER}
PAM_RUSER : ${PAM_RUSER}
Remote Host : ${PAM_RHOST}
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

