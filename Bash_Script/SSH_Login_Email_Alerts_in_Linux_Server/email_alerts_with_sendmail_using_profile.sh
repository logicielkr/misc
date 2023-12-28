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
    sendmail -f ${SENDER} ${RECIPIENT} <<EOF
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
