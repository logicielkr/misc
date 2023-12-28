#!/bin/bash

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

## exim or postfix instead of sendmail

sendmail -f ${SENDER} ${RECIPIENT} <<EOF
From: <${SENDER}>
To: <${RECIPIENT}>
Subject: ${subject}
	 
${message}
	 
EOF

unset SENDER
unset RECIPIENT
unset remote
unset user
unset host
unset subject
unset message
