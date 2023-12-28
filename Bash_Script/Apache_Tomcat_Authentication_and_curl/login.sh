#!/bin/bash

IFS=$'\r\n'

USERNAME=
PASSWORD=
PROTECTED_URL=

J_SECURITY_CHECK=$( curl -s -k --http1.1 -G ${PROTECTED_URL} | grep -o "j_security_check.*\" ")
J_SECURITY_CHECK_LENGTH=$( expr length $J_SECURITY_CHECK - 2 )
J_SECURITY_CHECK=${J_SECURITY_CHECK:0:$J_SECURITY_CHECK_LENGTH}
LOGIN_ACTION_URL="${PROTECTED_URL%/*}/${J_SECURITY_CHECK}"

LOGIN_DATA="j_username=${USERNAME}&j_password=${PASSWORD}"
HTTP_RESPONSE=$( curl -s -k --data "${LOGIN_DATA}" -X POST --http1.1 -i -L ${LOGIN_ACTION_URL} )

cookie=""
index=0
for line in $( echo "$HTTP_RESPONSE" | grep "^Set-Cookie" ); do
	index=`expr $index + 1`
	if (( $index > 1 )); then
		cookie="$cookie&${line:12}"
	else
		cookie="${line:12}"
	fi
done
echo $cookie
